package com.padaks.todaktodak.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationDtoResponse implements Serializable {
    private String token = "";
}
