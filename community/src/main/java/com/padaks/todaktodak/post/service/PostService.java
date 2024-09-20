package com.padaks.todaktodak.post.service;

import com.padaks.todaktodak.post.domain.Post;
import com.padaks.todaktodak.post.dto.PostListDto;
import com.padaks.todaktodak.post.dto.PostsaveDto;
import com.padaks.todaktodak.post.repository.PostRepository;
import com.padaks.todaktodak.util.S3ClientFileUpload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Member;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final S3ClientFileUpload s3ClientFileUpload;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName; // S3 버킷 이름 가져오기

    public void create(PostsaveDto dto, MultipartFile imageSsr){

        String imageUrl = null;
        if (dto.getPostImgUrl() != null && !dto.getPostImgUrl().isEmpty()){
         imageUrl = s3ClientFileUpload.upload(dto.getPostImgUrl(), bucketName);
        }
        Post post = dto.toEntity(imageUrl);
        postRepository.save(post);
    }

    public Page<PostListDto> postList(Pageable pageable){
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(a->a.listFromEntity());
    }
}
