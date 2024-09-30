package com.padaks.todaktodak.notification.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentCancelFailDto {
    private BigDecimal fee;
    private String impUid;
    private String memberEmail;
}
