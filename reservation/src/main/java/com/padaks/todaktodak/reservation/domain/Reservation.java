package com.padaks.todaktodak.reservation.domain;

import com.padaks.todaktodak.chatroom.domain.ChatRoom;
import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.hospital.domain.Hospital;
import com.padaks.todaktodak.medicalchart.domain.MedicalChart;
import com.padaks.todaktodak.reservationnotification.domain.Notification;
import com.padaks.todaktodak.review.domain.Review;
import lombok.*;

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
    private String doctorEmail;

    @Enumerated(EnumType.STRING)
    private ReserveType reservationType;

    private LocalDate reservationDate;

    private LocalTime reservationTime;

    private boolean isUntact;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private MedicalItem medicalItem;

    private String field; // 증상

    private String message;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @OneToMany(mappedBy = "reservation")
    private List<Notification> notificationList = new ArrayList<>();

    @OneToOne(mappedBy = "reservation")
    private Review review;

    @OneToOne(mappedBy = "reservation")
    private ChatRoom chatRoom;

    @OneToOne(mappedBy = "reservation")
    private MedicalChart medicalChart;


    public void updateStatus(Status status){
        this.status = status;
    }
}
