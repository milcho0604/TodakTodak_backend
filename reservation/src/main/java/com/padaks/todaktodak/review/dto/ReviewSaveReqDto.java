package com.padaks.todaktodak.review.dto;

import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReviewSaveReqDto {
    private String memberEmail;
    private String name;
    private int rating;
    private String contents;
    private Long reservationId;

    public Review toEntity(String email, Reservation reservation, String name){
        return Review.builder()
                .memberEmail(email)
                .name(name)
                .rating(this.rating)
                .contents(this.contents)
                .reservation(reservation)
                .build();
    }

}
