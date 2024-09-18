package com.padaks.todaktodak.review.controller;

import com.padaks.todaktodak.review.dto.CreateReviewReqDto;
import com.padaks.todaktodak.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<?> reviewCreate(@RequestBody CreateReviewReqDto dto){

        reviewService.reviewCreate(dto);

        return new ResponseEntity<>("작성 완료", HttpStatus.OK);
    }
}
