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
import javax.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "dayOfWeek is essential")
    private DayOfHoliday dayOfWeek;

    @NotEmpty(message = "openTime is essential")
    private LocalTime openTime;

    @NotEmpty(message = "closeTime is essential")
    private LocalTime closeTime;

    @NotEmpty(message = "untactType is essential")
    private Boolean untact;

    public void updateOperatingHours(DoctorOperatingHoursReqDto dto){
        //this.member = dto.getMember();
        this.dayOfWeek = dto.getDayOfWeek();
        this.openTime = dto.getOpenTime();
        this.closeTime = dto.getCloseTime();
        this.untact = dto.getUntact();
    }
}
