package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MontlyMemberCountDto {
    private Integer year;
    private Integer month;
    private Long memberCount;
    private Role role;
}
