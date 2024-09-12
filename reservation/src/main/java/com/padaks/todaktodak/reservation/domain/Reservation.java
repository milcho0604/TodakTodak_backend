package com.padaks.todaktodak.reservation.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.notification.domain.Notification;
import com.padaks.todaktodak.payment.domain.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Reservation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Column(nullable = false)
    private String memberEmail;
    @Column(nullable = false)
    private Long childId;
    @Column(nullable = false)
    private Long hospitalId;
    @Column(nullable = false)
    private String doctorEmail;

    private ReserveType reservationType;
    @Column(nullable = false)
    private LocalDate reservationDate;
    @Column(nullable = false)
    private LocalTime reservationTime;
    private Status status;
    private String message;

    @OneToMany(mappedBy = "reservation")
    private List<Notification> notificationList = new ArrayList<>();

    @OneToOne(mappedBy = "reservation")
    private Payment payment;

}
