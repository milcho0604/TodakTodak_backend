package com.padaks.todaktodak.payment.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuccessDto {
    private String impUid;
    private int price;
}
