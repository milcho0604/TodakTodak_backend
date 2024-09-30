package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.member.domain.Address;
import com.padaks.todaktodak.member.domain.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MemberUpdateReqDto {
    private String name;
    private String memberEmail;
    private String password;
    private String confirmPassword;
    private String phoneNumber;
    private Address address;
    private MultipartFile profileImage; // 프로필 이미지 추가
    private String profileImgUrl; // 기존 프로필 이미지 URL

    public static MemberUpdateReqDto fromEntity(Member member) {
        return MemberUpdateReqDto.builder()
                .name(member.getName())
                .memberEmail(member.getMemberEmail())
                .phoneNumber(member.getPhoneNumber())
                .address(member.getAddress())
                .profileImgUrl(member.getProfileImgUrl())
                .build();
    }
}
