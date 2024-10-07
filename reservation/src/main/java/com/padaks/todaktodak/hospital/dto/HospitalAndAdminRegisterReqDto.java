package com.padaks.todaktodak.hospital.dto;

import com.padaks.todaktodak.hospital.domain.Hospital;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalAndAdminRegisterReqDto {

    // 병원 register request DTO
    private String hospitalName; // 병원이름

    private String address; // 병원주소

    private String dong; // 병원주소(동)

    private BigDecimal latitude; // 위도

    private BigDecimal longitude; //경도

    private String hospitalPhoneNumber; // 병원전화번호

    private String businessRegistrationInfo; // 사업자등록번호

    // 병원 admin 회원가입 request DTO
    private String adminName; // 병원 admin 이름 (대표자이름)

    private String adminEmail; // 병원 admin 이메일

    private String adminPassword; // 병원 admin 비밀번호

    private String adminPhoneNumber; // 병원 admin 개인 전화번호


    public static Hospital toEntity(HospitalAndAdminRegisterReqDto dto){
        return Hospital.builder()
                .name(dto.getHospitalName()) // 병원이름
                .address(dto.getAddress()) // 병원주소
                .dong(dto.getDong()) // 병원주소(동)
                .latitude(dto.getLatitude()) // 위도
                .longitude(dto.getLongitude()) // 경도
                .phoneNumber(dto.getHospitalPhoneNumber()) // 병원전화번호
                .businessRegistrationInfo(dto.getBusinessRegistrationInfo()) // 사업자등록번호
                .representativeName(dto.getAdminName()) // 대표자 이름
                .adminEmail(dto.getAdminEmail()) // 병원 admin 이메일
                .representativePhoneNumber(dto.getAdminPhoneNumber()) // 대표자 핸드폰 번호
                .isAccept(false) // 개발자admin 승인전까지 승인여부 false
                .build();
    }
}
