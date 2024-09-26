package com.padaks.todaktodak.member.dto;

import lombok.Data;

@Data
public class MemberReportDto {
    private String memberEmail;
    private String name;
    private int reportCount;
}
