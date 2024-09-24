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
    private DayOfHoliday dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;
    @Enumerated(EnumType.STRING)
    private DayOfHoliday untack;

    public void updateOperatingHours(DoctorOperatingHoursReqDto dto){
        this.member = dto.getMember();
        this.dayOfWeek = dto.getDayOfWeek();
        this.openTime = dto.getOpenTime();
        this.closeTime = dto.getCloseTime();
        this.untack = dto.getUntack();
    }
}
