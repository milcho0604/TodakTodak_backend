package com.padaks.todaktodak.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)  // 정의되지 않은 필드는 무시
public class MemberLoginDto {
    private String memberEmail;
    private String password;
//    private String fcmToken;
    private boolean verified;
}
