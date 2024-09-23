package com.padaks.todaktodak.hospital.dto.HospitalDTO;

import com.padaks.todaktodak.common.enumdir.DayOfHoliday;
import com.padaks.todaktodak.hospital.domain.Hospital;
import com.padaks.todaktodak.hospitaloperatinghours.domain.HospitalOperatingHours;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalListResDto {
    private Long id; // 병원 id

    private Long standby; // 병원 실시간 대기자 수

    private String distance; // 내위치 ~ 병원 직선거리

    private String name; // 병원이름

    private String address; // 병원주소

    private String dong; // 병원주소(동)

    private String keywords; // 병원 keywords

    private DayOfHoliday dayOfWeek; // 요일

    private LocalTime openTime; // 영업시작 시각

    private LocalTime closeTime; // 영업종료 시각

    private String todaySchedule; // 오늘 영업시간 (최종)

    public static HospitalListResDto fromEntity(Hospital hospital,
                                                Long standby,
                                                String distance
                                                ){

        HospitalOperatingHours todayOperatingHours = null; // 오늘 병원영업시간
        String todaySchedule = null;
        List<HospitalOperatingHours> operatingHoursList = hospital.getHospitalOperatingHours();

        // 오늘날짜 요일
        String todayDayOfWeek = LocalDateTime.now().getDayOfWeek().toString();

        // 오늘날짜 요일에 해당하는 병원영업시간 찾기
        for(HospitalOperatingHours hours : operatingHoursList){
            if(hours.getDayOfWeek().toString().equalsIgnoreCase(todayDayOfWeek)){
                todayOperatingHours = hours;
                break;
            }
        }

        if (todayOperatingHours != null) {
            // ex : 월요일 9:00 ~ 18:00
            todaySchedule = todayOperatingHours.getDayOfWeek().getString()
                    + " "
                    + todayOperatingHours.getOpenTime()
                    + " ~ "
                    + todayOperatingHours.getCloseTime();
        } else {
            // 영업시간이 없는 경우 휴무 처리
            todaySchedule = "휴무";
        }

        return HospitalListResDto.builder()
                .id(hospital.getId())
                .standby(standby)
                .distance(distance)
                .name(hospital.getName())
                .address(hospital.getAddress())
                .dong(hospital.getDong())
                .keywords(hospital.getKeywords())
                .dayOfWeek(todayOperatingHours != null ? todayOperatingHours.getDayOfWeek() : null)
                .openTime(todayOperatingHours != null ? todayOperatingHours.getOpenTime() : null)
                .closeTime(todayOperatingHours != null ? todayOperatingHours.getCloseTime() : null)
                .todaySchedule(todaySchedule)
                .build();
    }
}
