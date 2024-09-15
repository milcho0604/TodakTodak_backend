package com.padaks.todaktodak.reservation.domain;

import com.padaks.todaktodak.charroom.domain.ChatRoom;
import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.medicalchart.domain.MedicalChart;
import com.padaks.todaktodak.notification.domain.Notification;
import com.padaks.todaktodak.payment.domain.Payment;
import com.padaks.todaktodak.review.domain.Review;
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
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "constraintName",
                        columnNames={"doctorEmail", "reservationDate", "reservationTime"}
                )
        }
)
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
    private boolean isUntact;
    private Status status;

    public enum MedicalItem{
        일반진료,
        예방접종
    }
    private MedicalItem medicalItem;
//    증상
    private String field;
    private String message;

    @OneToMany(mappedBy = "reservation")
    private List<Notification> notificationList = new ArrayList<>();

    @OneToOne(mappedBy = "reservation")
    private Review review;

    @OneToOne(mappedBy = "reservation")
    private ChatRoom chatRoom;

    @OneToOne(mappedBy = "reservation")
    private MedicalChart medicalChart;
}
