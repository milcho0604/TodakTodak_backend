package com.padaks.todaktodak.payment.dto;
import com.padaks.todaktodak.common.domain.Role;
import lombok.Data;

@Data
public class MemberPayDto {
    private String memberEmail;
    private String name;
    private String phoneNumber;
    private Role role;
}
