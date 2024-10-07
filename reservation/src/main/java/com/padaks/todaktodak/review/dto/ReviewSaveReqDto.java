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
    private String doctorName;
    private String doctorEmail;
    private int rating;
    private String contents;
    private Long reservationId;
    private boolean untact;


    public Review toEntity(String email, Reservation reservation, String name, String doctorEmail, boolean untact){
        return Review.builder()
                .memberEmail(email)
                .name(name)
                .doctorName(doctorName)
                .rating(this.rating)
                .contents(this.contents)
                .reservation(reservation)
                .doctorEmail(doctorEmail)
                .untact(untact)
                .build();
    }

}
