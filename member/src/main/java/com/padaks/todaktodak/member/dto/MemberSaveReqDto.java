package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.common.domain.DelYN;
import com.padaks.todaktodak.member.domain.Address;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSaveReqDto {
    private String name;
    private String email;
    private String password;
    private String profileImgUrl;
    private String phoneNumber;
    private String ssn;
    private Address address;

    @Builder.Default
    private Role role = Role.Member;
    private DelYN delYN = DelYN.N;

    public Member toEntity(String password) {
        return Member.builder()
                .password(password)
                .name(this.name)
                .email(this.email)
                .profileImgUrl(this.profileImgUrl)
                .phoneNumber(this.phoneNumber)
                .ssn(this.ssn)
                .address(this.address)
                .role(this.role)
                .delYN(this.delYN)
                .build();

    }
}
