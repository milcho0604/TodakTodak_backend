package com.padaks.todaktodak.notification.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentSuccessDto {
    private int fee;
    private String name;
    private String memberEmail;
    private String adminEmail;
}
