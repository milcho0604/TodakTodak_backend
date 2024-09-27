package com.padaks.todaktodak.notification.dto;

import lombok.Data;

@Data
public class PaymentFailDto {
    private int fee;
    private String name;
    private String memberEmail;
}
