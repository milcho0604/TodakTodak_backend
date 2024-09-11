package com.padaks.todaktodak.payment.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.reservation.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;
    @Column
    private String reservationMemberEmail;
    @Column
    private String requestUniqueNumber;
    @Column
    private String paymentUniqueNumber;
    @Column
    private String merchantCode;
    @Column
    private String merchantOrderNumber;
    @Column
    private String merchantMemberId;
    @Column
    private PaymentMethod paymentMethod;
    @Column
    private String productName;
    @Column
    private String productCode;
//    결제 생성 시간.
    @Column
    @CreationTimestamp
    private LocalDateTime requestTimeStamp;
//    결제 승인 시간
    @Column
    private LocalDateTime approvalTimeStamp;
    @Column
    private String responseDetails;
    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
