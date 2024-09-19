package com.padaks.todaktodak.hospitaloperatinghours.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.common.enumdir.DayOfHoliday;
import com.padaks.todaktodak.hospital.domain.Hospital;
import com.padaks.todaktodak.hospitaloperatinghours.dto.HospitalOperatingHoursReqDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class HospitalOperatingHours extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DayOfHoliday dayOfWeek; // 요일

    private LocalTime openTime; // 영업시작 시각

    private LocalTime closeTime; // 영업종료 시각

    private LocalTime breakStart; // 휴게시간 시작

    private LocalTime breakEnd; // 휴게시간 끝

    @OneToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital; // 병원id

    public void updateOperatingHours(HospitalOperatingHoursReqDto dto){
        this.dayOfWeek = dto.getDayOfWeek();
        this.openTime = dto.getOpenTime();
        this.closeTime = dto.getCloseTime();
        this.breakStart = dto.getBreakStart();
        this.breakEnd = dto.getBreakEnd();
    }
}
