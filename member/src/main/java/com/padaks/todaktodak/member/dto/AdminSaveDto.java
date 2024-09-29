package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminSaveDto {
    private String memberEmail;
    private String name;
    private String phoneNumber;
    private String password;
    private Role role;
    private boolean verified;
}
