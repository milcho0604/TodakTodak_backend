package com.padaks.todaktodak.comment.service;

import com.padaks.todaktodak.comment.domain.Comment;
import com.padaks.todaktodak.comment.dto.CommentDetailDto;
import com.padaks.todaktodak.comment.dto.CommentSaveDto;
import com.padaks.todaktodak.comment.repository.CommentRepository;
import com.padaks.todaktodak.communitynotification.controller.SseController;
import com.padaks.todaktodak.communitynotification.domain.Notification;
import com.padaks.todaktodak.communitynotification.repository.NotificationRepository;
import com.padaks.todaktodak.post.domain.Post;
import com.padaks.todaktodak.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;
    private SseController sseController;

    public Comment createComment(CommentSaveDto dto){

        Comment savedComment;
        if (dto.getPostId() != null){
            Post post = postRepository.findById(dto.getPostId()).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 post입니다."));
            savedComment = commentRepository.save(dto.toEntity(post));

            //post 작성자에게 알림 전송
//            Notification notification = new Notification();
//            notification = notification.saveDto();
//            notificationRepository.save(notification);
//            sseController.publishMessage(notification);

        }else {
            throw new IllegalArgumentException("답변을 위한 POST ID가 필요합니다.");
        }
        return savedComment;
    }

    public List<CommentDetailDto> getCommentByPostId(Long postId){
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(comment -> CommentDetailDto.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .doctorEmail(comment.getDoctorEmail())
                        .createdTimeAt(comment.getCreatedTimeAt())
                        .build())
                .collect(Collectors.toList());
    }

    public Page<CommentDetailDto> CommentListByDoctorId(String doctorEmail, Pageable pageable){
        Page<Comment> comments = commentRepository.findByDoctorEmail(doctorEmail, pageable);
        return comments.map(a->a.listFromEntity());
    }

    public void deleteComment(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 comment입니다."));
        comment.updateDeleteAt();
    }

}