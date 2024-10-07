package com.padaks.todaktodak.common.dto;

import lombok.Data;

@Data
public class MemberFeignDto {
    private String memberEmail;
//    private String name;
    private int reportCount;
    private String Role;
}
