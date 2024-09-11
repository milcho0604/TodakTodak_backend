package com.padaks.todaktodak.review.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.reservation.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Review extends BaseTimeEntity {

    @EmbeddedId
    @Column(name = "review_id")
    private ReviewId id;

    @MapsId("reservationId")
    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @Column(nullable = false)
    @Check(constraints = "value BETWEEN 1 AND 5")
    private int rating;
    @Column
    private String contents;

}
