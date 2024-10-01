package com.padaks.todaktodak.notification.dto;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;

@Data
public class PaymentFailDto {
    private BigDecimal fee;
    private String impUid;
    private String memberEmail;
    private String adminEmail;
}
