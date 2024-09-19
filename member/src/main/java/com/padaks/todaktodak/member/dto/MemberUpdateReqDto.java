package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.member.domain.Address;
import com.padaks.todaktodak.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateReqDto {
    private String name;
    private String memberEmail;
    private String password;
    private String confirmPassword;
    private String phone;
    private Address address;
    private MultipartFile profileImage; // 프로필 이미지 추가

    public Member toEntity(Member member, String existingProfileImgUrl) {
        return Member.builder()
                .memberEmail(member.getMemberEmail())
                .name(this.name)
                .password(this.password)
                .phoneNumber(this.phone)
                .address(this.address)
                .profileImgUrl(existingProfileImgUrl)
                .build();
    }
}
