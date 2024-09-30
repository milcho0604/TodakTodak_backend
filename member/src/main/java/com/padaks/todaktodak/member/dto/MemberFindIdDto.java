package com.padaks.todaktodak.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberFindIdDto {
    private String name;
    private String phoneNumber;
}
