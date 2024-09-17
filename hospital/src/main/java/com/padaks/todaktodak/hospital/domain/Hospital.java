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
    private String name; // 병원이름

    @Column(nullable = false, unique = true)
    private String address; // 병원주소

    @Column(nullable = false, unique = true)
    private String phoneNumber; // 병원번호

    private String hospitalImageUrl; // 병원사진 url

    private String description; // 병원소개

    private String notice; // 병원공지

    @Column(precision = 9, scale = 6) // 최대 9자리, 소수점 이하 6자리
    private BigDecimal latitude; // 위도

    @Column(precision = 9, scale = 6)
    private BigDecimal longitude; // 경도

    private String businessRegistrationInfo; // 사업자등록번호

    private String representativeName; // 대표자 이름

    private String representativePhoneNumber; // 대표자 핸드폰 번호

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL)
    private List<Doctor> doctor;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL)
    private List<HospitalOperatingHours> hospitalOperatingHours;


    // 병원 이미지 URL 업데이트
    public void updateHospitalImageUrl(String hospitalImageUrl) {
        this.hospitalImageUrl = hospitalImageUrl;
    }
}
