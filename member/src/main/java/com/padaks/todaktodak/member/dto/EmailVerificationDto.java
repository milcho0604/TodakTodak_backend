package com.padaks.todaktodak.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationDto implements Serializable {
    private String memberEmail = "";
    private String verificationToken = "";
    private String verificationNumber = "";
    private int attemptCount = 0;
    private boolean isDone = false;

    public EmailVerificationDto(String memberEmail, String verificationToken, String verificationNumber) {
        this.memberEmail = memberEmail;
        this.verificationToken = verificationToken;
        this.verificationNumber = verificationNumber;
    }

    public EmailVerificationDtoResponse toResponse() {
        return new EmailVerificationDtoResponse(this.verificationToken);
    }
}