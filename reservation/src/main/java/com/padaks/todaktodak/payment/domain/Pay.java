package com.padaks.todaktodak.payment.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.medicalchart.domain.MedicalChart;
import com.padaks.todaktodak.payment.dto.PaymentListResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    private String customerUid;  // 고객별 고유 ID (정기 결제용)

    private String buyerName;
    private String buyerTel;
    private String merchantUid;

    private String name;

    @Column(nullable = false)
    private String impUid;  // 아임포트 고유 번호

    @Column(nullable = false)
    private BigDecimal amount;  // 결제 금액

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;  // 결제 상태

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;  // 결제 방법 (단독, 정기)

    @CreationTimestamp
    private LocalDateTime requestTimeStamp;  // 결제 요청 시간

    private LocalDateTime approvalTimeStamp;  // 결제 승인 시간

    @OneToOne
    @JoinColumn(name = "medical_chart_id")
    private MedicalChart medicalChart;  // 연관된 의료 기록 (MedicalChart)

    // 정기결제일
    private LocalDateTime subscriptionEndDate;

    private Integer count;

    // 정기 결제 종료일 체크 및 결제 상태 업데이트 메소드
    public boolean isSubscriptionActive() {
        return LocalDateTime.now().isAfter(subscriptionEndDate) && this.getDeletedAt() == null;
    }

    public void updateNextPaymentDate(String impUid, Integer count) {
        // 결제 주기 로직에 따라 다음 결제일 갱신과 impUid 업데이트
        this.subscriptionEndDate = this.subscriptionEndDate.plusMonths(1);
        this.impUid = impUid;
        this.count = count;
    }

    // 최초 결제시 구독이라면 !
    public void changeSubscriptionEndDate(LocalDateTime subscriptionEndDate, PaymentStatus paymentStatus){
        this.subscriptionEndDate = subscriptionEndDate;
        this.paymentStatus = paymentStatus;
    }


    public PaymentListResDto listFromEntity() {
        return PaymentListResDto.builder()
                .id(this.id)
                .name(this.name)
                .buyerName(this.buyerName)
                .customerUid(this.customerUid)
                .memberEmail(this.memberEmail)
                .amount(this.amount)
                .buyerTel(this.buyerTel)
                .impUid(this.impUid)
                .merchantUid(this.merchantUid)
                .approvalTimeStamp(this.approvalTimeStamp)
                .paymentMethod(this.paymentMethod)
                .paymentStatus(this.paymentStatus)
                .build();
    }

    public void cancelPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // 구독 취소
    public void cancelSubscription() {
        updateDeleteAt();
        this.paymentStatus = PaymentStatus.UNSUBSCRIBE;

    }



}
