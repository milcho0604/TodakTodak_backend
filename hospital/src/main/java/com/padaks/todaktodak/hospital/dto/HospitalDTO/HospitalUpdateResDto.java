package com.padaks.todaktodak.hospital.dto.HospitalDTO;

import com.padaks.todaktodak.hospital.domain.Hospital;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalUpdateResDto {
    private Long id; // 병원 id

    private String name; // 병원이름

    private String address; // 병원주소

    private String dong; // 병원주소(동)

    private String phoneNumber; // 병원번호

    private String hospitalImageUrl; // 병원사진

    private String description; // 병원소개

    private String notice; // 병원공지

    private BigDecimal latitude; // 위도

    private BigDecimal longitude; //경도

    private String businessRegistrationInfo; // 사업자등록번호

    private String representativeName; // 대표자 이름

    private String representativePhoneNumber; // 대표자 핸드폰 번호

    public static HospitalUpdateResDto fromEntity(Hospital hospital){
        return HospitalUpdateResDto.builder()
                .id(hospital.getId())
                .name(hospital.getName())
                .address(hospital.getAddress())
                .dong(hospital.getDong())
                .phoneNumber(hospital.getPhoneNumber())
                .hospitalImageUrl(hospital.getHospitalImageUrl())
                .description(hospital.getDescription())
                .notice(hospital.getNotice())
                .latitude(hospital.getLatitude())
                .longitude(hospital.getLongitude())
                .businessRegistrationInfo(hospital.getBusinessRegistrationInfo())
                .representativeName(hospital.getRepresentativeName())
                .representativePhoneNumber(hospital.getRepresentativePhoneNumber())
                .build();
    }
}
