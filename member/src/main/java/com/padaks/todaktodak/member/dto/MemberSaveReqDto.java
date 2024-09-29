package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.member.domain.Address;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSaveReqDto {
    private String name;
    private String memberEmail;
    private String password;
    private String profileImgUrl;
    private String phoneNumber;
    private String ssn;
    private Address address;
    private MultipartFile profileImage;

    @Builder.Default
    private boolean verified = true;


    @Builder.Default
    private Role role = Role.Member;

    public Member toEntity(String password) {
        return Member.builder()
                .password(password)
                .name(this.name)
                .memberEmail(this.memberEmail)
                .profileImgUrl(this.profileImgUrl)
                .phoneNumber(this.phoneNumber)
                .ssn(this.ssn)
                .address(this.address)
                .role(this.role)
                .build();
    }
}

