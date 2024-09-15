package com.padaks.todaktodak.member.dto;


import com.padaks.todaktodak.member.domain.Address;
import com.padaks.todaktodak.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateReqDto {
    private String name;
    private String password;
    private String confirmPassword;
    private String phone;
    private Address address;

    public Member toEntity() {
        return Member.builder()
                .name(this.name)
                .password(this.password)
                .phoneNumber(this.phone)
                .address(this.address)
                .build();
    }
}
