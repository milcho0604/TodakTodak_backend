package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResDto {

    private Long id;
    private String name;
    private Role role;

    public MemberResDto fromEntity(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.role = member.getRole();
        return this;
    }
}
