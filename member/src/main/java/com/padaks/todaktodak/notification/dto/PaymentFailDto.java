package com.padaks.todaktodak.notification.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentFailDto {
    private BigDecimal fee;
    private String impUid;
    private String memberEmail;
    private String adminEmail;
}
