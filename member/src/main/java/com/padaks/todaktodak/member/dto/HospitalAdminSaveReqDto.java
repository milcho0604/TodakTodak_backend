package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.member.domain.Address;
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
public class HospitalAdminSaveReqDto {
    // 병원 admin 회원가입 request DTO
    private String adminName; // 병원 admin 이름 (대표자이름)

    private String adminEmail; // 병원 admin 이메일

    private String adminPassword; // 병원 admin 비밀번호

    private String adminPhoneNumber; // 병원 admin 전화번호

    private Long hospitalId; // 저장된 병원 id
    @Builder.Default
    private boolean verified = true;

    public static Member toEntity(HospitalAdminSaveReqDto dto,
                                  String encodedPassword, Address address, String bio){
        return Member.builder()
                .name(dto.getAdminName())
                .memberEmail(dto.getAdminEmail())
                .password(encodedPassword) // 암호화된 비밀번호
                .phoneNumber(dto.getAdminPhoneNumber())
                .hospitalId(dto.getHospitalId())
                .role(Role.NonUser)
                .isVerified(dto.verified)
                .address(address)
                .bio(bio)
                .build();
    }
}
