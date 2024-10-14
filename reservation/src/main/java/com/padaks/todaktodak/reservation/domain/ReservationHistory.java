package com.padaks.todaktodak.reservation.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private String memberName;
    @Column(nullable = false)
    private Long childId;
    @Column(nullable = false)
    private Long hospitalId;
    @Column(nullable = false)
    private String doctorEmail;
    @Column(nullable = false)
    private String doctorName;
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
