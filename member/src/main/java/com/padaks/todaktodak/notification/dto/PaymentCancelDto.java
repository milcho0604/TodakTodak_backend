package com.padaks.todaktodak.notification.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentCancelDto {
    private BigDecimal fee;
    private String name;
    private String memberEmail;
    private String adminEmail;

}
