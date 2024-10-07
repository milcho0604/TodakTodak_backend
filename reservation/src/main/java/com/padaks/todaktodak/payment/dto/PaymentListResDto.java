package com.padaks.todaktodak.payment.dto;

import com.padaks.todaktodak.payment.domain.PaymentMethod;
import com.padaks.todaktodak.payment.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentListResDto {
    private Long id;
    private String customerUid;  // 고객별 고유 ID (정기 결제용)
    private String name;
    private String memberEmail;
    private String buyerName;
    private String buyerTel;
    private String impUid;
    private String merchantUid;
    private BigDecimal amount;
    private LocalDateTime approvalTimeStamp;
    private PaymentMethod paymentMethod;  // 결제 방식
    private PaymentStatus paymentStatus;  // 결제 상태
}