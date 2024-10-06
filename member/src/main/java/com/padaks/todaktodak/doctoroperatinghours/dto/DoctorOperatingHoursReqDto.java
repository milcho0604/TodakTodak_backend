package com.padaks.todaktodak.doctoroperatinghours.dto;

import com.padaks.todaktodak.common.enumdir.DayOfHoliday;
import com.padaks.todaktodak.doctoroperatinghours.domain.DoctorOperatingHours;
import com.padaks.todaktodak.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorOperatingHoursReqDto {
//    private Member member;
//    private Long doctorId;
    private DayOfHoliday dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Boolean untact;

    public static DoctorOperatingHours toEntity(Member doctor, DoctorOperatingHoursReqDto dto){
        return DoctorOperatingHours.builder()
                .member(doctor)
//                .id(dto.getDoctorId())
                .dayOfWeek(dto.getDayOfWeek())
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .untact(dto.getUntact())
                .build();

    }
}
