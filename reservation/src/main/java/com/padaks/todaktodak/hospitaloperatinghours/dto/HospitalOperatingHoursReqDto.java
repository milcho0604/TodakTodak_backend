package com.padaks.todaktodak.hospitaloperatinghours.dto;

import com.padaks.todaktodak.common.enumdir.DayOfHoliday;
import com.padaks.todaktodak.hospital.domain.Hospital;
import com.padaks.todaktodak.hospitaloperatinghours.domain.HospitalOperatingHours;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalOperatingHoursReqDto {
    private DayOfHoliday dayOfWeek; // 요일

    private LocalTime openTime; // 영업시작 시각

    private LocalTime closeTime; // 영업종료 시각

    private LocalTime breakStart; // 휴게시간 시작

    private LocalTime breakEnd; // 휴게시간 끝

    public static HospitalOperatingHours toEntity(Hospital hospital,
                                                  HospitalOperatingHoursReqDto dto){
        return HospitalOperatingHours.builder()
                .hospital(hospital)
                .dayOfWeek(dto.getDayOfWeek())
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .breakStart(dto.getBreakStart())
                .breakEnd(dto.getBreakEnd())
                .build();
    }
}
