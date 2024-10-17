package com.padaks.todaktodak.common.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfoDto {
    private Long id;
    private String name;
    private String memberEmail;
    private String profileImgUrl;
    private String role;
    private Long hospitalId;
}
