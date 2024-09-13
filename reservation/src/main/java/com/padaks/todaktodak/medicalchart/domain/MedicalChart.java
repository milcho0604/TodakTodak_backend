package com.padaks.todaktodak.medicalchart.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.payment.domain.Payment;
import com.padaks.todaktodak.reservation.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MedicalChart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_record_id")
    private Long id;
    private int fee;
    public enum PaymentStatus{
        결제요청,
        결제완료
    }
    private PaymentStatus paymentStatus;
    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @OneToOne(mappedBy = "medicalChart")
    private Payment payment;
}
