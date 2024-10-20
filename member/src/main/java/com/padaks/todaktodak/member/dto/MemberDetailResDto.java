package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MemberDetailResDto {

    private Long id;
    private String name;
    private String memberEmail;
    private String profileImgUrl;
    private Role role;
    private boolean verified;

    public static MemberDetailResDto fromEntity(Member member){
        return MemberDetailResDto.builder()
                .id(member.getId())
                .name(member.getName())
                .memberEmail(member.getMemberEmail())
                .profileImgUrl(member.getProfileImgUrl())
                .role(member.getRole())
                .verified(member.isVerified())
                .build();
    }
}
