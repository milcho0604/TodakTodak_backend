package com.padaks.todaktodak.hospital.dto;

import com.padaks.todaktodak.common.enumdir.DayOfHoliday;
import com.padaks.todaktodak.hospital.domain.Hospital;
import com.padaks.todaktodak.hospitaloperatinghours.domain.HospitalOperatingHours;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalAdminDetailResDto {

    //=== 병원기본정보 ===

    private Long id; // 병원 id


    private String name; // 병원이름

    private String address; // 병원주소

    private String dong; // 병원주소(동)

    private String phoneNumber; // 병원번호

    private String hospitalImageUrl; // 병원사진

    private String description; // 병원소개

    private String notice; // 병원공지

    private String keywords; // 병원 keywords

    private String businessRegistrationInfo; // 사업자등록번호

    private String representativeName; // 대표자 이름

    private String representativePhoneNumber; // 대표자 핸드폰 번호

    private Long untactFee; // 비대면진료비

    private Boolean isAccept; // 가입승인여부

    // === 병원 리뷰정보 ===
    private Double averageRating; // 병원 평균 평점

    private Long reviewCount; // 병원 리뷰 개수

    private BigDecimal latitude; // 위도

    private BigDecimal longitude; //경도


    public static HospitalAdminDetailResDto fromEntity(Hospital hospital,
                                                       Double averageRating,
                                                       Long reviewCount
                                                  ){

        // 평균평점 소수점 첫째 자리까지 반올림 처리
        if (averageRating != null) {
            averageRating = Math.round(averageRating * 10) / 10.0;
        }

        // 병원 영업시간 리스트 순회

        return HospitalAdminDetailResDto.builder()
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
                .keywords(hospital.getKeywords())
                .businessRegistrationInfo(hospital.getBusinessRegistrationInfo())
                .representativeName(hospital.getRepresentativeName())
                .representativePhoneNumber(hospital.getRepresentativePhoneNumber())
                .untactFee(hospital.getUntactFee())
                .isAccept(hospital.getIsAccept())
                .averageRating(averageRating)  // 평균 평점
                .reviewCount(reviewCount)  // 리뷰 개수
                .build();
    }

}
