package com.padaks.todaktodak.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentListDto {
    private Long id;
//    private String memberName;
    private String memberEmail;
    private int amount;
    private LocalDateTime approvalTimeStamp;
}