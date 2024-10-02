package com.padaks.todaktodak.review.dto;

import com.padaks.todaktodak.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListResDto {
    private Long id;
    private String name;
    private String hospitalName;
    private String doctorName;
    private int rating;
    private String contents;
    private LocalDateTime createdAt;

}
