package com.padaks.todaktodak.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.padaks.todaktodak.common.domain.Role;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberFeignDto {
    private String memberEmail;
    private String name;
    private String phoneNumber;
    private Role role;
    private Long hospitalId;
}
