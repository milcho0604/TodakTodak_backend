package com.padaks.todaktodak.hospital.dto;

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

    public static HospitalAdminSaveReqDto fromDto(HospitalAndAdminRegisterReqDto dto,
                                                  Long hospitalId){
        return HospitalAdminSaveReqDto.builder()
                .adminName(dto.getAdminName())
                .adminEmail(dto.getAdminEmail())
                .adminPassword(dto.getAdminPassword())
                .adminPhoneNumber(dto.getAdminPhoneNumber())
                .hospitalId(hospitalId)
                .build();
    }
}
