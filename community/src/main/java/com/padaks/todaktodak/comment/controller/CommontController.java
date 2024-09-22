package com.padaks.todaktodak.comment.controller;

import com.padaks.todaktodak.comment.dto.CommentDetailDto;
import com.padaks.todaktodak.comment.dto.CommentSaveDto;
import com.padaks.todaktodak.comment.dto.CommentUpdateReqDto;
import com.padaks.todaktodak.comment.service.CommentService;
import com.padaks.todaktodak.common.dto.CommonErrorDto;
import com.padaks.todaktodak.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("comment")
public class CommontController {
    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<?> register(@RequestBody CommentSaveDto dto){
        try {
            commentService.createComment(dto);
            if (dto.getPostId() != null){
                CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED,"Comment 등록 성공", dto.getPostId());
                return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
            }else {
                throw new IllegalArgumentException("postId must be provided.");
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/listBydoctorEmail")
    public ResponseEntity<?> commentList(@RequestBody CommentDetailDto dto, Pageable pageable){
        try {
            Page<CommentDetailDto> comments = commentService.CommentListByDoctorId(dto.getDoctorEmail(), pageable);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "comment 목록 조회 성공", comments);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody CommentUpdateReqDto dto){
        try {
            commentService.updateComment(id, dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "comment가 성공적으로 수정되었습니다.", id);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id){
        try {
            commentService.deleteComment(id);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "comment가 성공적으로 삭제되었습니다.", id);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }
}
