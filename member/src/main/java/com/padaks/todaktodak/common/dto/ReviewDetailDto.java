package com.padaks.todaktodak.common.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDetailDto {
    private double averageRating;   // 리뷰 평균 점수
    private long count1Star;        // 1점 리뷰 수
    private long count2Stars;       // 2점 리뷰 수
    private long count3Stars;       // 3점 리뷰 수
    private long count4Stars;       // 4점 리뷰 수
    private long count5Stars;       // 5점 리뷰 수
}
