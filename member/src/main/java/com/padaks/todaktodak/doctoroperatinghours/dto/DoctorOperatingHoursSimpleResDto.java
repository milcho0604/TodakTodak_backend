package com.padaks.todaktodak.doctoroperatinghours.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.padaks.todaktodak.common.enumdir.DayOfHoliday;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime openTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime closeTime;
    private boolean untact;
}
