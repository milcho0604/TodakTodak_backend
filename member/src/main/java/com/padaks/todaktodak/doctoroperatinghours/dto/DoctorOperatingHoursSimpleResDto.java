package com.padaks.todaktodak.doctoroperatinghours.dto;

import com.padaks.todaktodak.common.enumdir.DayOfHoliday;
import com.padaks.todaktodak.doctoroperatinghours.domain.DoctorOperatingHours;
import com.padaks.todaktodak.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorOperatingHoursSimpleResDto {
    private Long id;
    private String doctorName;
    private DayOfHoliday dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean untact;
}
