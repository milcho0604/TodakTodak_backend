package com.padaks.todaktodak.doctor.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailVerificationDto {
    private String memberEmail;
    private String code;
}
