package com.padaks.todaktodak.comment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.padaks.todaktodak.comment.domain.Comment;
import com.padaks.todaktodak.comment.dto.CommentDetailDto;
import com.padaks.todaktodak.comment.dto.CommentSaveDto;
import com.padaks.todaktodak.comment.dto.CommentUpdateReqDto;
import com.padaks.todaktodak.comment.dto.ReplyCommentSaveDto;
import com.padaks.todaktodak.comment.repository.CommentRepository;
import com.padaks.todaktodak.common.dto.MemberFeignDto;
import com.padaks.todaktodak.common.dto.MemberInfoDto;
import com.padaks.todaktodak.common.feign.HospitalFeignClient;
import com.padaks.todaktodak.common.feign.MemberFeignClient;
import com.padaks.todaktodak.post.domain.Post;
import com.padaks.todaktodak.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final MemberFeignClient memberFeignClient;

    //member 객체 리턴
    public MemberFeignDto getMemberInfo() {
        MemberFeignDto member = memberFeignClient.getMemberEmail();
        return member;
    }

//    댓글 작성 메소드
    public Comment createComment(CommentSaveDto dto){
        MemberFeignDto member = getMemberInfo(); //현재 로그인한 사용자 정보
        Comment savedComment;
        int reportCount = member.getReportCount();
        // 신고 횟수가 5 이상일 경우 예외 처리
        if (reportCount >= 5) {
            throw new IllegalArgumentException("신고 횟수가 5회 이상인 회원은 댓글을 작성할 수 없습니다.");
        }

        if (dto.getPostId() != null){
            Post post = postRepository.findById(dto.getPostId()).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 post입니다."));
            //댓글 작성 제한 (post 작성자 / Role.Doctor 인 사용자)
            if (!post.getMemberEmail().equals(member.getMemberEmail()) && !"DOCTOR".equals(member.getRole())){
                throw  new IllegalArgumentException("댓글을 작성할 수 있는 권한이 없습니다.");
            }
            String name;
            if (!member.getRole().equals("DOCTOR")){
                name = maskSecondCharacter(member.getName());
                savedComment = dto.toEntity(post, null, member.getMemberEmail(), name, member.getProfileImgUrl());
            }else {
                name = member.getName() + "원장";
                savedComment = dto.toEntity(post, null, member.getMemberEmail(), name, member.getProfileImgUrl());
            }
            commentRepository.save(savedComment);
            String type = "COMMENT";
            notificationComment(post.getMemberEmail(), post.getTitle(), post.getId(), type);
        }else {
            throw new IllegalArgumentException("답변을 위한 POST ID가 필요합니다.");
        }
        return savedComment;
    }

//    대댓글 작성 API
    public Comment createReplyComment(ReplyCommentSaveDto dto){
        MemberFeignDto member = getMemberInfo();
        Comment savedComment;
        int reportCount = member.getReportCount();
        if (reportCount >= 5) {
            throw new IllegalArgumentException("신고 횟수가 5회 이상인 회원은 대댓글을 작성할 수 없습니다.");
        }
        if (dto.getPostId() != null){
            Post post = postRepository.findById(dto.getPostId()).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 post입니다."));
            Comment parentComment = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다."));
            if(parentComment.getParent() != null){
                throw new IllegalArgumentException("대댓글에는 댓글이 허용되지 않습니다.");
            }
            String name;
            if (!member.getRole().equals("DOCTOR")){
                name = maskSecondCharacter(member.getName());
                savedComment = dto.toEntity(post, parentComment, member.getMemberEmail(), name, member.getProfileImgUrl());
            }else {
                name = member.getName() + "원장";
                savedComment = dto.toEntity(post, parentComment, member.getMemberEmail(), name, member.getProfileImgUrl());
            }
            commentRepository.save(savedComment);
            String type = "REPLY_COMMENT";
            notificationComment(post.getMemberEmail(), post.getTitle(), post.getId(), type);
            notificationComment(parentComment.getDoctorEmail(), post.getTitle(), post.getId(), type);
        }else {
            throw new IllegalArgumentException("답변을 위한 POST ID가 필요합니다.");
        }
        return savedComment;
    }

//    알림 로직
    private void notificationComment(String receiver, String title, Long postId, String type){
        Map<String, Object> messageData = new HashMap<>();
        //fcm 메세지 데이터 객체 생성
        messageData.put("receiverEmail", receiver);
        messageData.put("postId", postId);
        messageData.put("title", title);
        messageData.put("type", type);

        try {
            String message = objectMapper.writeValueAsString(messageData);
            kafkaTemplate.send("community-success", message);
            log.info("comment 등록 성공 메세지를 kafka로 전송: {}", message);
        } catch (JsonProcessingException e) {
            log.error("JSON 변환 오류: {}", e.getMessage());
            kafkaTemplate.send("community-fail", "JSON 변환 오류가 발생했습니다.");
        }
    }

    public List<CommentDetailDto> getCommentByPostId(Long postId){
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream()
                .filter(comment -> comment.getDeletedTimeAt() == null)
                .map(comment -> CommentDetailDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .doctorEmail(comment.getDoctorEmail())
//                        .name(maskSecondCharacter(memberFeignClient.getMemberName(comment.getDoctorEmail()).getName()))
                        .name(comment.getName())
                        .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                        .profileImg(comment.getProfileImg())
                        .createdTimeAt(comment.getCreatedTimeAt())
                        .updatedTimeAt(comment.getUpdatedTimeAt())
                        .build())
                .collect(Collectors.toList());
    }

    public Page<CommentDetailDto> CommentListByDoctorId(String doctorEmail, Pageable pageable){
        Page<Comment> comments = commentRepository.findByDoctorEmailAndDeletedTimeAtIsNull(doctorEmail, pageable);
        return comments.map(a->a.listFromEntity());
    }

    public void updateComment(Long id, CommentUpdateReqDto dto){
        MemberFeignDto member = getMemberInfo(); //현재 로그인 되어있는 사용자
        Comment comment = commentRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 comment입니다."));
        int reportCount = member.getReportCount();
        // 신고 횟수가 5 이상일 경우 예외 처리
        if (reportCount >= 5) {
            throw new IllegalArgumentException("신고 횟수가 5회 이상인 회원은 댓글을 수정할 수 없습니다.");
        }

        String type = "COMMENT";
        Map<String, Object> messageData = new HashMap<>();
        //현재 로그인 되어있는 사용자가 comment의 작성자인 경우
        if (!Objects.equals(member.getMemberEmail(), comment.getDoctorEmail())){
            throw new IllegalArgumentException("작성자 이외에는 수정할 수 없습니다.");
        }
            comment.update(dto);
            commentRepository.save(comment);
            //fcm 메세지 데이터 객체 생성
            messageData.put("receiverEmail", comment.getDoctorEmail());
            messageData.put("postId", dto.getPostId());
            messageData.put("type", type);

            try {
                String message = objectMapper.writeValueAsString(messageData);
                kafkaTemplate.send("community-success", message);
                log.info("comment 수정 성공 메세지를 kafka로 전송: {}", message);
            } catch (JsonProcessingException e) {
                log.error("JSON 변환 오류: {}", e.getMessage());
                kafkaTemplate.send("community-fail", "JSON 변환 오류가 발생했습니다.");
            }

    }

    public void deleteComment(Long id){
        MemberFeignDto member = getMemberInfo();
        Comment comment = commentRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 comment입니다."));
        int reportCount = member.getReportCount();
        // 신고 횟수가 5 이상일 경우 예외 처리

        if (!member.getMemberEmail().equals(comment.getDoctorEmail())){
            throw new IllegalArgumentException("작성자 이외에는 삭제할 수 없습니다.");
        }
        if (reportCount >= 5) {
            throw new IllegalArgumentException("신고 횟수가 5회 이상인 회원은 댓글을 삭제할 수 없습니다.");
        }
        comment.updateDeleteAt();
    }

    // 이름을 마스킹 처리해서 저장하는 메서드
    public static String maskSecondCharacter(String name) {
        // 이름이 2글자 이상일 경우 두 번째 글자 마스킹 처리
        if (name.length() >= 1) {
            return name.charAt(0) + "*" + name.substring(2);
        }
        return name;
    }

}