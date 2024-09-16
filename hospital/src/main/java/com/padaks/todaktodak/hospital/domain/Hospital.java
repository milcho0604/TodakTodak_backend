package com.padaks.todaktodak.hospital.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.common.enumdir.Option;
import com.padaks.todaktodak.doctor.domain.Doctor;
import com.padaks.todaktodak.hospitaloperatinghours.domain.HospitalOperatingHours;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Hospital extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, unique = true)
    private String address;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    @Column()
    @ColumnDefault("0")
    private int doctorCount;
    @Column
    @Enumerated(EnumType.STRING)
    private Option isOperating;
//    병원 소개
    private String description;
//    병원 공지
    private String notice;
    @Column(precision = 9, scale = 6)
    private BigDecimal latitude;
    @Column(precision = 9, scale = 6)
    private BigDecimal longitude;
//    사업자 정보
    private String businessRegistrationInfo;
//    대표자 이름
    private String representativeName;
//    대표자 핸드폰 번호
    private String representativePhoneNumber;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL)
    private List<Doctor> doctor;

    @OneToOne(mappedBy = "hospital", cascade = CascadeType.ALL)
    private HospitalOperatingHours hospitalOperatingHours;
}
