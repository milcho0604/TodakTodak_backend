package com.padaks.todaktodak.hospitaloperatinghours.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.common.enumdir.DayOfHoliday;
import com.padaks.todaktodak.hospital.domain.Hospital;
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

    private DayOfHoliday dayOfHoliday;

    private LocalTime openTime;
    private LocalTime closeTime;

    private LocalTime breakStart;
    private LocalTime breakEnd;

    @OneToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;
}
