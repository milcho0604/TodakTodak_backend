package com.padaks.todaktodak.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JavaEmailVerificationDto {
    private String memberEmail;
    private String code;
}
