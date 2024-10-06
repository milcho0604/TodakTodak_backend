package com.padaks.todaktodak.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewMyListResDto {
    private String hospitalName;
    private String doctorName;
    private int rating;
    private String contents;
    private LocalDateTime createdAt;
}
