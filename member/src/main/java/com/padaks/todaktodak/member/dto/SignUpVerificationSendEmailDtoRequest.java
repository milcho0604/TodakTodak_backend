package com.padaks.todaktodak.member.dto;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
public class SignUpVerificationSendEmailDtoRequest {
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String memberEmail;
}