package com.padaks.todaktodak.doctoroperatinghours.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.common.enumdir.DayOfHoliday;
import com.padaks.todaktodak.doctor.domain.Doctor;
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
    @JoinColumn(name = "doctor_id")
    private Doctor Did;
    private DayOfHoliday dayOfHoliday;
    private LocalTime openTime;
    private LocalTime closeTime;
    private DayOfHoliday untack;

}
