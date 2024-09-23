package com.padaks.todaktodak.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private String memberEmail;
    private String name;
    private String phoneNumber;
}
