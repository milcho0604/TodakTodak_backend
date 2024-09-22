package com.padaks.todaktodak.report.controller;

import com.padaks.todaktodak.comment.repository.CommentRepository;
import com.padaks.todaktodak.common.dto.CommonErrorDto;
import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.post.repository.PostRepository;
import com.padaks.todaktodak.report.dto.ReportSaveReqDto;
import com.padaks.todaktodak.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @PostMapping("/create")
    public ResponseEntity<?> register(@RequestBody ReportSaveReqDto dto){
        try{
            reportService.create(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "신고 등록 성공", dto.getReporterEmail());
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        }catch (IllegalArgumentException | EntityNotFoundException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }
}
