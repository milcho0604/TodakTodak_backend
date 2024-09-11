package com.padaks.todaktodak.review.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@Data
public class ReviewId implements Serializable {

    private Long reservationId;
    private Long reviewSeq;

    public ReviewId(Long reservationId, Long reviewSeq){
        this.reservationId = reservationId;
        this.reviewSeq = reviewSeq;
    }
}
