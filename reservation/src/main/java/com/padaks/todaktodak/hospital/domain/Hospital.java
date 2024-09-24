package com.padaks.todaktodak.hospital.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.hospital.dto.HospitalDTO.HospitalUpdateReqDto;
import com.padaks.todaktodak.hospitaloperatinghours.domain.HospitalOperatingHours;
import com.padaks.todaktodak.reservation.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String dong; // 병원주소(동)

    @Column(nullable = false, unique = true)
    private String phoneNumber; // 병원번호

    private String hospitalImageUrl; // 병원사진 url

    private String description; // 병원소개

    private String notice; // 병원공지

    private String keywords; // 병원 keywords

    @Column(precision = 9, scale = 6) // 최대 9자리, 소수점 이하 6자리
    private BigDecimal latitude; // 위도

    @Column(precision = 9, scale = 6)
    private BigDecimal longitude; // 경도

    private String businessRegistrationInfo; // 사업자등록번호

    private String representativeName; // 대표자 이름

    private String representativePhoneNumber; // 대표자 핸드폰 번호

    private Long untactFee; // 비대면진료비

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL)
    private List<HospitalOperatingHours> hospitalOperatingHours;

    @OneToMany(mappedBy = "hospital")
    private List<Reservation> reservations;

    //== Custom methods ==//
    // 병원 이미지 URL 업데이트
    public void updateHospitalImageUrl(String hospitalImageUrl) {
        this.hospitalImageUrl = hospitalImageUrl;
    }

    public void updateHospitalInfo(HospitalUpdateReqDto dto){
        this.name = dto.getName();
        this.address = dto.getAddress();
        this.dong = dto.getDong();
        this.phoneNumber = dto.getPhoneNumber();
        this.description = dto.getDescription();
        this.notice = dto.getNotice();
        this.keywords = dto.getKeywords();
        this.latitude = dto.getLatitude();
        this.longitude = dto.getLongitude();
        this.businessRegistrationInfo = dto.getBusinessRegistrationInfo();
        this.representativeName = dto.getRepresentativeName();
        this.representativePhoneNumber = dto.getRepresentativePhoneNumber();
        this.untactFee = dto.getUntactFee();
    }
}
