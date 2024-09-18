package com.padaks.todaktodak.review.dto;

import com.padaks.todaktodak.reservation.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateReviewReqDto {

    private Long id;
    private int rating;
    private String contents;
}
