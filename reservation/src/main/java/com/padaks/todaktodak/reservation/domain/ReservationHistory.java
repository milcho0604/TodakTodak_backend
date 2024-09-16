package com.padaks.todaktodak.reservation.domain;

import com.padaks.todaktodak.charroom.domain.ChatRoom;
import com.padaks.todaktodak.medicalchart.domain.MedicalChart;
import com.padaks.todaktodak.notification.domain.Notification;
import com.padaks.todaktodak.review.domain.Review;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservationHistory {
    @Id
    @Column(name = "reservation_history_id")
    private Long id;

    @Column(nullable = false)
    private String memberEmail;
    @Column(nullable = false)
    private Long childId;
    @Column(nullable = false)
    private Long hospitalId;
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
    //    증상
    private String field;
    private String message;

    @CreationTimestamp
    private LocalDateTime reservationDeletedTime;
}
