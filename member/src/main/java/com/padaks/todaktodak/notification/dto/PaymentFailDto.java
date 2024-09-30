package com.padaks.todaktodak.notification.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentFailDto {
    private BigDecimal fee;
    private String impUid;
    private String memberEmail;
}
