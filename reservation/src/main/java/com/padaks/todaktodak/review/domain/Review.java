package com.padaks.todaktodak.review.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.reservation.domain.Reservation;
import lombok.*;
import org.hibernate.annotations.Check;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false)
    @Check(constraints = "value BETWEEN 1 AND 5")
    private int rating;

    @Column
    private String contents;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

}
