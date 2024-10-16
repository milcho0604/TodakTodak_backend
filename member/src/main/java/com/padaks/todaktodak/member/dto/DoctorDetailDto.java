package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.doctoroperatinghours.dto.DoctorOperatingHoursSimpleResDto;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDetailDto {
    private Long doctorId;
    private String doctorName;
    private String doctorImageUrl;
    private String doctorEmail;
    private String bio;
    private String hospitalName;
    private String hospitalAddress;
    private String hospitalImageUrl;
    private long reviewCount; // 리뷰 개수
    private double reviewPoint;//리뷰 평점
    private List<DoctorOperatingHoursSimpleResDto> operatingHours; //근무시간 리스트

    public DoctorDetailDto fromEntity(Member doctor, HospitalInfoDto hospitalInfoDto, double reviewRate, long totalCount, List<DoctorOperatingHoursSimpleResDto> operatingHours) {
        this.doctorId = doctor.getId();
        this.doctorName = doctor.getName();
        this.doctorImageUrl = doctor.getProfileImgUrl();
        this.doctorEmail = doctor.getMemberEmail();
        this.bio = doctor.getBio();
        this.hospitalName = hospitalInfoDto.getName();
        this.hospitalAddress = hospitalInfoDto.getAddress();
        this.hospitalImageUrl = hospitalInfoDto.getProfileImg();
        this.reviewCount = totalCount;
        this.reviewPoint = reviewRate;
        this.operatingHours = operatingHours;
        return this;
    }
}
