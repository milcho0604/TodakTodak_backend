package com.padaks.todaktodak.payment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {

    private Long id;
    private String memberEmail;
    private String impUid;
    private int amount;
    private String paymentStatus;  // 문자열로 상태 전달 해야함
    private String paymentMethod;
    private LocalDateTime requestTimeStamp;
    private LocalDateTime approvalTimeStamp;
    private String responseDetails;
}
