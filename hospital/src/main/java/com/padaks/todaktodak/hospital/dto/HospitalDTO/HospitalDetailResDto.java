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
public class HospitalDetailResDto {

    private Long id; // 병원 id

    private Long standby; // 병원 실시간 대기자 수

    private String distance; // 내위치 ~ 병원 직선거리

    private String name; // 병원이름

    private String address; // 병원주소

    private String dong; // 병원주소(동)

    private String phoneNumber; // 병원번호

    private String hospitalImageUrl; // 병원사진

    private String description; // 병원소개

    private String notice; // 병원공지

    private String keywords; // 병원 keywords

    private BigDecimal latitude; // 위도 (나중에 프론트에서 지도 마커로 표시)

    private BigDecimal longitude; //경도

    private String businessRegistrationInfo; // 사업자등록번호

    private String representativeName; // 대표자 이름

    private String representativePhoneNumber; // 대표자 핸드폰 번호

    private Long untactFee; // 비대면진료비

    public static HospitalDetailResDto fromEntity(Hospital hospital,
                                                  Long standby,
                                                  String distance
                                                  ){
        return HospitalDetailResDto.builder()
                .id(hospital.getId())
                .standby(standby) // 실시간 대기자 수
                .distance(distance)
                .name(hospital.getName())
                .address(hospital.getAddress())
                .dong(hospital.getDong())
                .phoneNumber(hospital.getPhoneNumber())
                .hospitalImageUrl(hospital.getHospitalImageUrl())
                .description(hospital.getDescription())
                .notice(hospital.getNotice())
                .keywords(hospital.getKeywords())
                .latitude(hospital.getLatitude())
                .longitude(hospital.getLongitude())
                .businessRegistrationInfo(hospital.getBusinessRegistrationInfo())
                .representativeName(hospital.getRepresentativeName())
                .representativePhoneNumber(hospital.getRepresentativePhoneNumber())
                .untactFee(hospital.getUntactFee())
                .build();
    }

}
