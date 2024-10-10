package com.padaks.todaktodak.notification.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;

@Data
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor
@Builder
public class PaymentFailDto {
    private BigDecimal fee;
    private String impUid;
    private String memberEmail;
    private String adminEmail;
}
