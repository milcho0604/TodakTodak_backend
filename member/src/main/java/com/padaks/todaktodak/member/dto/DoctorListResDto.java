package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorListResDto {
    private Long id;
    private String name;
    private String profileImgUrl;
    private Role role;
}
