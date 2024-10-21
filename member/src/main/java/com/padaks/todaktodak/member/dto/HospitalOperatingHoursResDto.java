package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.common.enumdir.DayOfHoliday;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalOperatingHoursResDto {
    private Long id; // 영업시간 id

    private DayOfHoliday dayOfWeek; // 요일

    private LocalTime openTime; // 영업시작 시간

    private LocalTime closeTime; // 영업종료 시간

    private LocalTime breakStart; // 휴게 작 시간

    private LocalTime breakEnd; // 휴게종료 시간

}
