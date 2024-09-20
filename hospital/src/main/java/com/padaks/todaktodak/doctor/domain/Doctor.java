package com.padaks.todaktodak.doctor.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.doctoroperatinghours.domain.DoctorOperatingHours;
import com.padaks.todaktodak.hospital.domain.Hospital;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String doctorEmail;

    private String profileImgUrl;

    private String bio;

    // 근무 시간
    @OneToOne(mappedBy = "doctor", cascade = CascadeType.ALL)
    private DoctorOperatingHours DoctorOperatingHours;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;
}
