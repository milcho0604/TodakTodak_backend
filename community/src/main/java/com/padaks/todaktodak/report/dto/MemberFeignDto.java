package com.padaks.todaktodak.report.dto;

import lombok.Data;

@Data
public class MemberFeignDto {
    private String memberEmail;
    private String name;
    private int reportCount;
}
