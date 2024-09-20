package com.padaks.todaktodak.post.controller;

import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.post.dto.PostsaveDto;
import com.padaks.todaktodak.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("post")
public class PostController {
    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<?> register(PostsaveDto dto, @RequestPart(value = "image", required = false)MultipartFile imageSsr){
        System.out.println("여기여기여기");
        try {
            postService.create(dto, imageSsr);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "post 등록 성공", null));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResDto(HttpStatus.BAD_REQUEST, "post 등록에 실패했습니다." + e.getMessage(), null));
        }
    }



}
