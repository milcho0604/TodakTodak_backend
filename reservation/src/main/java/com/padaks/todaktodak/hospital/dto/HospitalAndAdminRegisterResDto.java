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
public class HospitalAndAdminRegisterResDto {

    private Long id; // 병원id

    private String hospitalName; // 병원이름

    private String address; // 병원주소

    private String dong; // 병원주소(동)

    private BigDecimal latitude; // 위도

    private BigDecimal longitude; //경도

    private String hospitalPhoneNumber; // 병원전화번호

    private String businessRegistrationInfo; // 사업자등록번호

    private String hospitalAdminEmail; // 병원 admin 회원 이메일

    private String representativeName; // 대표자 이름

    private String representativePhoneNumber; // 대표자 핸드폰 번호

    private Boolean isAccept; // 가입승인여부

    private Long hospitalAdminId; // 병원 admin 회원 id

    public static HospitalAndAdminRegisterResDto fromEntity(Hospital hospital,
                                                            Long hospitalAdminId){
        return HospitalAndAdminRegisterResDto.builder()
                .id(hospital.getId())
                .hospitalName(hospital.getName())
                .address(hospital.getAddress())
                .dong(hospital.getDong())
                .latitude(hospital.getLatitude())
                .longitude(hospital.getLongitude())
                .hospitalPhoneNumber(hospital.getPhoneNumber())
                .businessRegistrationInfo(hospital.getBusinessRegistrationInfo())
                .hospitalAdminEmail(hospital.getAdminEmail())
                .representativeName(hospital.getRepresentativeName())
                .representativePhoneNumber(hospital.getRepresentativePhoneNumber())
                .isAccept(hospital.getIsAccept())
                .hospitalAdminId(hospitalAdminId)
                .build();
    }

}
