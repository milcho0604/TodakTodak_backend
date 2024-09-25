package com.padaks.todaktodak.common.dto;

import com.padaks.todaktodak.common.domain.Role;
import lombok.Data;

@Data
public class MemberFeignDto {
    private String memberEmail;
    private String name;
    private String phoneNumber;
    private Role role;
}
