package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.domain.Role;
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
    private Role role;
    private Long hospitalId;

    public MemberInfoDto fromEntity(Member member){
        this.id = member.getId();
        this.name = member.getName();
        this.memberEmail = member.getMemberEmail();
        this.role = member.getRole();
        this.hospitalId = member.getHospitalId();
        this.profileImgUrl = member.getProfileImgUrl();
        return this;
    }
}
