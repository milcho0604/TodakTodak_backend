package com.padaks.todaktodak.medicalchart.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.payment.domain.Pay;
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
    @Column(name = "medical_chart_id")
    private Long id;

    private int fee;

    public enum MedicalStatus{
        진료중,
        진료완료,
        결제완료;
    }
    @Enumerated(EnumType.STRING)
    private MedicalStatus medicalStatus;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @OneToOne(mappedBy = "medicalChart")
    private Pay payment;

    public void complete() {
        this.medicalStatus = MedicalStatus.진료완료;
    }

    public void pay() {
        this.medicalStatus = MedicalStatus.결제완료;
    }
}
