package com.padaks.todaktodak.post.service;

import com.padaks.todaktodak.comment.dto.CommentDetailDto;
import com.padaks.todaktodak.comment.service.CommentService;
import com.padaks.todaktodak.common.feign.MemberFeignClient;
import com.padaks.todaktodak.post.domain.Post;
import com.padaks.todaktodak.post.dto.*;
import com.padaks.todaktodak.post.repository.PostRepository;
import com.padaks.todaktodak.report.dto.MemberFeignDto;
import com.padaks.todaktodak.util.S3ClientFileUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final MemberFeignClient memberPostFeignClient;
    private final PostRepository postRepository;
    private final S3ClientFileUpload s3ClientFileUpload;
    private final CommentService commentService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName; // S3 버킷 이름 가져오기

    //member 객체 리턴, 토큰 포함
    public MemberFeignDto getMemberInfo(){
        MemberFeignDto member = memberPostFeignClient.getMemberEmail();
        return member;
    }

    public void create(PostsaveDto dto){

        MultipartFile postImage = dto.getPostImage();
        MemberFeignDto member = getMemberInfo();
        String memberEmail = member.getMemberEmail();


        String imageUrl = s3ClientFileUpload.upload(postImage, bucketName);

        Post post = dto.toEntity(imageUrl, memberEmail);
        postRepository.save(post);
    }

    public Page<PostListDto> postList(Pageable pageable){
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(a->a.listFromEntity());
    }

    public PostDetailDto getPostDetail(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("존재하지 않는 post입니다."));

        List<CommentDetailDto> comments = commentService.getCommentByPostId(id);
        PostDetailDto postDetailDto = PostDetailDto.fromEntity(post, comments);
        return postDetailDto;
    }

    @Transactional
    public void updatePost(Long id, PostUpdateReqDto dto){
        Post post = postRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 Post입니다."));
        MultipartFile image = dto.getPostImg();
        if (image != null && !image.isEmpty()){
            String imageUrl = s3ClientFileUpload.upload(image, bucketName);
            post.updateImage(imageUrl);
        }
        post.update(dto);
        postRepository.save(post);
    }

    public void deletePost(Long id){
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 post입니다."));
        post.updateDeleteAt();
    }
}

