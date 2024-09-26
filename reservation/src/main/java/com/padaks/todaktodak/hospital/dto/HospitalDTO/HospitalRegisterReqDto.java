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
public class HospitalRegisterReqDto {

    private String name; // 병원이름

    private String address; // 병원주소

    private String dong; // 병원주소(동)

    private String phoneNumber; // 병원번호

    private MultipartFile hospitalImage; // 병원사진

    private String description; // 병원소개

    private String notice; // 병원공지

    private String keywords; // 병원 keywords

    private BigDecimal latitude; // 위도

    private BigDecimal longitude; //경도

    private String businessRegistrationInfo; // 사업자등록번호

    private String representativeName; // 대표자 이름

    private String representativePhoneNumber; // 대표자 핸드폰 번호

    private Long untactFee; // 비대면진료비

    public static Hospital toEntity(HospitalRegisterReqDto dto,
                               String hospitalImageUrl
                                ){
        return Hospital.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .dong(dto.getDong())
                .phoneNumber(dto.getPhoneNumber())
                .description(dto.getDescription())
                .keywords(dto.getKeywords())
                .notice(dto.getNotice())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .businessRegistrationInfo(dto.getBusinessRegistrationInfo())
                .representativeName(dto.getRepresentativeName())
                .representativePhoneNumber(dto.getRepresentativePhoneNumber())
                .untactFee(dto.getUntactFee())
                .hospitalImageUrl(hospitalImageUrl) // 업로드한 이미지의 URL을 저장
                .build();
    }

}
