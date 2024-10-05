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
public class HospitalDetailResDto {

    //=== 병원기본정보 ===

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

    private Boolean isAccept; // 가입승인여부

    // === 병원 리뷰정보 ===
    private Double averageRating; // 병원 평균 평점

    private Long reviewCount; // 병원 리뷰 개수

    // === 오늘 영업정보 ===
    private DayOfHoliday dayOfWeek; // 요일

    private LocalTime openTime; // 영업시작 시각

    private LocalTime closeTime; // 영업종료 시각

    private String todaySchedule; // 오늘 영업시간 (최종)

    // === 일주일 영업정보 ===

    private String Monday; // 월요일 영업시간
    private String Tuesday; // 화요일 영업시간
    private String Wednesday; // 수요일 영업시간
    private String Thursday; // 목요일 영업시간
    private String Friday; // 금요일 영업시간
    private String Saturday; // 토요일 영업시간
    private String Sunday; // 일요일 영업시간


    public static HospitalDetailResDto fromEntity(Hospital hospital,
                                                  Long standby,
                                                  String distance,
                                                  Double averageRating,
                                                  Long reviewCount
                                                  ){

        // 평균평점 소수점 첫째 자리까지 반올림 처리
        if (averageRating != null) {
            averageRating = Math.round(averageRating * 10) / 10.0;
        }

        // 일주일간의 영업시간 정보 초기화
        String mondaySchedule = "휴무";
        String tuesdaySchedule = "휴무";
        String wednesdaySchedule = "휴무";
        String thursdaySchedule = "휴무";
        String fridaySchedule = "휴무";
        String saturdaySchedule = "휴무";
        String sundaySchedule = "휴무";

        HospitalOperatingHours todayOperatingHours = null; // 오늘 병원영업시간
        String todaySchedule = "휴무"; // 오늘영업시간(최종), 영업시간이 없는 경우 휴무 처리

        // 해당 병원의 영업시간 리스트
        List<HospitalOperatingHours> operatingHoursList = hospital.getHospitalOperatingHours();

        // 오늘날짜 요일
        String todayDayOfWeek = LocalDateTime.now().getDayOfWeek().toString();

        // 병원 영업시간 리스트 순회
        for(HospitalOperatingHours hours : operatingHoursList){
            String dayOfWeek = hours.getDayOfWeek().getKey(); // 요일을 문자열로 가져옴

            // 오늘의 영업시간 정보
            if (dayOfWeek.equalsIgnoreCase(todayDayOfWeek)) {
                todayOperatingHours = hours;
                // ex : 월요일 9:00 ~ 18:00
                todaySchedule = hours.getDayOfWeek().getValue() + " " + hours.getOpenTime() + " ~ " + hours.getCloseTime();
            }

            // 각 요일별 영업시간 설정
            switch (dayOfWeek) {
                case "MONDAY":
                    mondaySchedule = "월요일 " + hours.getOpenTime() + " ~ " + hours.getCloseTime();
                    break;
                case "TUESDAY":
                    tuesdaySchedule = "화요일 " + hours.getOpenTime() + " ~ " + hours.getCloseTime();
                    break;
                case "WEDNESDAY":
                    wednesdaySchedule = "수요일 " + hours.getOpenTime() + " ~ " + hours.getCloseTime();
                    break;
                case "THURSDAY":
                    thursdaySchedule = "목요일 " + hours.getOpenTime() + " ~ " + hours.getCloseTime();
                    break;
                case "FRIDAY":
                    fridaySchedule = "금요일 " + hours.getOpenTime() + " ~ " + hours.getCloseTime();
                    break;
                case "SATURDAY":
                    saturdaySchedule = "토요일 " + hours.getOpenTime() + " ~ " + hours.getCloseTime();
                    break;
                case "SUNDAY":
                    sundaySchedule = "일요일 " + hours.getOpenTime() + " ~ " + hours.getCloseTime();
                    break;
            }

        }

        return HospitalDetailResDto.builder()
                .id(hospital.getId())
                .standby(standby)
                .distance(distance) // TODO : 실시간 대기자 수
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
                .isAccept(hospital.getIsAccept())
                .averageRating(averageRating)  // 평균 평점
                .reviewCount(reviewCount)  // 리뷰 개수
                .dayOfWeek(todayOperatingHours != null ? todayOperatingHours.getDayOfWeek() : null)  // 오늘의 요일
                .openTime(todayOperatingHours != null ? todayOperatingHours.getOpenTime() : null)  // 오늘의 영업 시작 시간
                .closeTime(todayOperatingHours != null ? todayOperatingHours.getCloseTime() : null)  // 오늘의 영업 종료 시간
                .todaySchedule(todaySchedule)  // 오늘의 영업 시간
                .Monday(mondaySchedule)  // 월요일 영업시간
                .Tuesday(tuesdaySchedule)  // 화요일 영업시간
                .Wednesday(wednesdaySchedule)  // 수요일 영업시간
                .Thursday(thursdaySchedule)  // 목요일 영업시간
                .Friday(fridaySchedule)  // 금요일 영업시간
                .Saturday(saturdaySchedule)  // 토요일 영업시간
                .Sunday(sundaySchedule)  // 일요일 영업시간
                .build();
    }

}
