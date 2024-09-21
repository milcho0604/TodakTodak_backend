package com.padaks.todaktodak.reservationnotification.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.reservation.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(nullable = false)
    private String memberEmail;
    @Column(nullable = false)
    private String category;
    private String content;
    @ColumnDefault("false")
    private Boolean isRead;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
