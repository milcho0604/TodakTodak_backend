package com.padaks.todaktodak.notification.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentCancelDto {
    private BigDecimal fee;
    private String name;
    private String memberEmail;
    private String adminEmail;

}
