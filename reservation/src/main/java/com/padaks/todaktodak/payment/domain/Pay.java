package com.padaks.todaktodak.payment.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.medicalchart.domain.MedicalChart;
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
public class Pay extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(nullable = false)
    private String memberEmail;  // 결제하는 사용자의 이메일

    @Column(nullable = false)
    private String impUid;  // 아임포트 고유 번호

    @Column(nullable = false)
    private int amount;  // 결제 금액

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;  // 결제 상태

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;  // 결제 방법 (CARD, BANK 등)

    @CreationTimestamp
    private LocalDateTime requestTimeStamp;  // 결제 요청 시간

    private LocalDateTime approvalTimeStamp;  // 결제 승인 시간

    @OneToOne
    @JoinColumn(name = "medical_chart_id")
    private MedicalChart medicalChart;  // 연관된 의료 기록 (MedicalChart)

    @Column(length = 1000)
    private String responseDetails;  // 결제 처리에 대한 응답 세부 정보
}
