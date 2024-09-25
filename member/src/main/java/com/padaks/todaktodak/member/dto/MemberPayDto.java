package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberPayDto {
    private String memberEmail;
    private String name;
    private String phoneNumber;
    private Role role;
}
