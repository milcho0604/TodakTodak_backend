package com.padaks.todaktodak.notification.dto;

import lombok.*;

@Data
public class PaymentSuccessDto {
    private int fee;
    private String name;
    private String memberEmail;
}
