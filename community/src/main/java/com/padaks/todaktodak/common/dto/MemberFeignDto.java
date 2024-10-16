package com.padaks.todaktodak.common.dto;

import lombok.Data;

@Data
public class MemberFeignDto {
    private String memberEmail;
    private String name;
    private String phoneNumber;
    private String role;
    private int reportCount;
    private Long hospitalId;
}
