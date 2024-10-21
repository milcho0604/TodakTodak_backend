package com.padaks.todaktodak.doctoroperatinghours.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.common.enumdir.DayOfHoliday;
import com.padaks.todaktodak.doctoroperatinghours.dto.DoctorOperatingHoursReqDto;
import com.padaks.todaktodak.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DoctorOperatingHours extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_operating_hours")
    private Long id;
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Enumerated(EnumType.STRING)
    private DayOfHoliday dayOfWeek; //요일
    private LocalTime openTime; //영업시작 시간
    private LocalTime closeTime; //영업종료 시간
    private Boolean untact; // 비대면여부
    private LocalTime breakStart; //휴게시작 시간
    private LocalTime breakEnd; //휴게종료시간

    public void updateOperatingHours(DoctorOperatingHoursReqDto dto){
        //this.member = dto.getMember();
        this.dayOfWeek = dto.getDayOfWeek();
        this.openTime = dto.getOpenTime();
        this.closeTime = dto.getCloseTime();
        this.untact = dto.getUntact();
        this.breakStart= dto.getBreakStart();
        this.breakEnd = dto.getBreakEnd();
    }
}
