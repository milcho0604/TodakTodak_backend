package com.padaks.todaktodak.post.service;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.padaks.todaktodak.comment.dto.CommentDetailDto;
import com.padaks.todaktodak.comment.service.CommentService;
import com.padaks.todaktodak.common.dto.HospitalNameFeignDto;
import com.padaks.todaktodak.common.dto.MemberFeignNameDto;
import com.padaks.todaktodak.common.dto.MemberInfoDto;
import com.padaks.todaktodak.common.feign.HospitalFeignClient;
import com.padaks.todaktodak.common.feign.MemberFeignClient;
import com.padaks.todaktodak.post.domain.Post;
import com.padaks.todaktodak.post.dto.*;
import com.padaks.todaktodak.post.repository.PostRepository;
import com.padaks.todaktodak.common.dto.MemberFeignDto;
import com.padaks.todaktodak.common.util.S3ClientFileUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final MemberFeignClient memberPostFeignClient;
    private final PostRepository postRepository;
    private final S3ClientFileUpload s3ClientFileUpload;
    private final CommentService commentService;
    @Qualifier("7") // Redis 퀄리파이어 설정
    private final RedisTemplate<String, Object> redisTemplate;

    //member 객체 리턴, 토큰 포함
    public MemberFeignDto getMemberInfo(){
        MemberFeignDto member = memberPostFeignClient.getMemberEmail();
        return member;
    }

    public void create(PostsaveDto dto){

        MultipartFile postImage = dto.getPostImage(); //게시글 사진

        MemberFeignDto member = getMemberInfo();
        String memberEmail = member.getMemberEmail();
        String profileImgUrl = member.getProfileImgUrl();
        int reportCount = member.getReportCount();

        // 신고 횟수가 5 이상일 경우 예외 처리
        if (reportCount >= 5) {
            throw new IllegalArgumentException("신고 횟수가 5회 이상인 회원은 포스트를 작성할 수 없습니다.");
        }



        String name;
        if (!member.getRole().equals("DOCTOR")){
            name = maskSecondCharacter(member.getName());
        }else {
            name = member.getName() + " 의사";
        }

        Post post;
        if(postImage != null){
            String imageUrl = s3ClientFileUpload.upload(postImage);
            post = dto.toEntity(imageUrl, memberEmail, name, profileImgUrl);
            postRepository.save(post);
        }else {
            post = dto.toEntity(null, memberEmail, name, profileImgUrl);
            postRepository.save(post);
        }


    }

    // 게시글 리스트
    public List<PostListDto> postList(){
        List<Post> posts = postRepository.findByDeletedTimeAtIsNull();
        // Post -> PostListDto로 변환
        return posts.stream().map(post -> {
            Long viewCount = getPostViews(post.getId());   // Redis에서 조회수 가져오기
            Long likeCount = getPostLikesCount(post.getId());   // Redis에서 좋아요 수 가져오기
            return post.listFromEntity(viewCount, likeCount);   // 조회수와 좋아요 수를 포함한 DTO로 변환
        }).collect(Collectors.toList());   // 리스트로 변환
    }

    // my post list !
    public Page<PostListDto> myPostList(Pageable pageable){
        String memberEmail = memberPostFeignClient.getMemberEmail().getMemberEmail();
        Page<Post> posts = postRepository.findByMemberEmailAndDeletedTimeAtIsNull(memberEmail, pageable);
        return posts.map(post -> {
            Long viewCount = getPostViews(post.getId());   // Redis에서 조회수 가져오기
            Long likeCount = getPostLikesCount(post.getId());   // Redis에서 좋아요 수 가져오기
            return post.listFromEntity(viewCount, likeCount);   // 조회수와 좋아요 수를 포함한 DTO로 변환
        });
    }

    public PostDetailDto getPostDetail(Long id){

        Post post = postRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("존재하지 않는 post입니다."));


        // 조회수 증가 로직 추가
        incrementPostViews(id);
        Long viewCount = getPostViews(id);
        Long likeCount = getPostLikesCount(id);

        PostDetailDto postDetailDto = PostDetailDto.fromEntity(post, viewCount, likeCount);
        return postDetailDto;
    }

    @Transactional
    public void updatePost(Long id, PostUpdateReqDto dto){
        Post post = postRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 Post입니다."));
        MemberFeignDto member = getMemberInfo();
        int reportCount = member.getReportCount();

        // 신고 횟수가 5 이상일 경우 예외 처리
        if (reportCount >= 5) {
            throw new IllegalArgumentException("신고 횟수가 5회 이상인 회원은 포스트를 수정할 수 없습니다.");
        }

        MultipartFile image = dto.getPostImg();
        if (image != null && !image.isEmpty()){
            String imageUrl = s3ClientFileUpload.upload(image);
            post.updateImage(imageUrl);
        }
        post.update(dto);
        postRepository.save(post);
    }

    public void deletePost(Long id){
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 post입니다."));
        MemberFeignDto member = getMemberInfo();
        int reportCount = member.getReportCount();
        if (reportCount >= 5) {
            throw new IllegalArgumentException("신고 횟수가 5회 이상인 회원은 포스트를 삭제할 수 없습니다.");
        }
        post.updateDeleteAt();
    }

    // Redis 조회수 증가 로직
    public void incrementPostViews(Long postId) {
        MemberFeignDto member = getMemberInfo();
        String memberEmail = member.getMemberEmail();

        String key = "post:views:" + postId;
        String userKey = "post:views:users:" + postId;

        Boolean hasViewed = redisTemplate.opsForSet().isMember(userKey, memberEmail);
        if (!Boolean.TRUE.equals(hasViewed)) {
            redisTemplate.opsForValue().increment(key, 1);  // 조회수 1 증가
//            redisTemplate.opsForSet().add(userKey, memberEmail);  // 중복 방지용 유저 이메일 저장 추후 추가할 수 있음
        }
    }

    // Redis 조회수 조회
    public Long getPostViews(Long postId) {
        String key = "post:views:" + postId;
        Object value = redisTemplate.opsForValue().get(key);
        if (value instanceof Integer) {
            return ((Integer) value).longValue();  // Integer를 Long으로 변환
        } else if (value instanceof Long) {
            return (Long) value;
        } else {
            return 0L;
        }
    }


    // Redis 좋아요 추가
    public void likePost(Long postId) {
        MemberFeignDto member = getMemberInfo();
        String memberEmail = member.getMemberEmail();

        String key = "post:likes:" + postId;
        String userKey = "post:likes:users:" + postId;

        Boolean hasLiked = redisTemplate.opsForSet().isMember(userKey, memberEmail);
        if (!Boolean.TRUE.equals(hasLiked)) {
            redisTemplate.opsForSet().add(key, memberEmail);  // 좋아요 누른 사용자 저장
            redisTemplate.opsForSet().add(userKey, memberEmail);  // 중복 방지용 이메일 저장
        }
    }

    // Redis 좋아요 취소
    public void unlikePost(Long postId) {
        MemberFeignDto member = getMemberInfo();
        String memberEmail = member.getMemberEmail();

        String key = "post:likes:" + postId;
        String userKey = "post:likes:users:" + postId;

        Boolean hasLiked = redisTemplate.opsForSet().isMember(userKey, memberEmail);
        if (Boolean.TRUE.equals(hasLiked)) {
            redisTemplate.opsForSet().remove(key, memberEmail);  // 좋아요 취소
            redisTemplate.opsForSet().remove(userKey, memberEmail);  // 중복 방지 이메일 제거
        }
    }

    // Redis 좋아요 수 조회
    public Long getPostLikesCount(Long postId) {
        String key = "post:likes:" + postId;
        Object value = redisTemplate.opsForSet().size(key);
        if (value instanceof Integer) {
            return ((Integer) value).longValue();  // Integer를 Long으로 변환
        } else if (value instanceof Long) {
            return (Long) value;
        } else {
            return 0L;
        }
    }

    // 이름을 마스킹 처리해서 저장하는 메서드
    public static String maskSecondCharacter(String name) {
        // 이름이 2글자 이상일 경우 두 번째 글자 마스킹 처리
        if (name.length() >= 1) {
            return name.charAt(0) + "*" + name.substring(2);
        }
        return name;
    }

//   좋아요 많은 게시글
    public List<PostListDto> famousPostList() {
        List<Post> posts = postRepository.findByDeletedTimeAtIsNull();
        // Post -> PostListDto로 변환
        List<PostListDto> postListDtoList = posts.stream().map(post -> {
            Long viewCount = getPostViews(post.getId());   // Redis에서 조회수 가져오기
            Long likeCount = getPostLikesCount(post.getId());   // Redis에서 좋아요 수 가져오기
            return post.listFromEntity(viewCount, likeCount);   // 조회수와 좋아요 수를 포함한 DTO로 변환
        }).collect(Collectors.toList());// 리스트로 변환

        postListDtoList.sort(Comparator.comparingDouble(PostListDto::getLikeCount).reversed());
        if (postListDtoList.size() > 3) {
            postListDtoList = postListDtoList.subList(0,3);
        }
        return postListDtoList;
    }

    public boolean likedByMember(Long postId) {
        MemberFeignDto member = getMemberInfo();
        String memberEmail = member.getMemberEmail();

        String key = "post:likes:" + postId;
        String userKey = "post:likes:users:" + postId;

        return redisTemplate.opsForSet().isMember(userKey, memberEmail);
    }
}

