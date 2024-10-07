package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.doctoroperatinghours.dto.DoctorOperatingHoursSimpleResDto;
import com.padaks.todaktodak.member.domain.Address;
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
public class DoctorListResDto {
    private Long id;
    private String name;
    private String profileImgUrl;
    private Role role;
    private String bio;
    private List<DoctorOperatingHoursSimpleResDto> operatingHours; //근무시간 리스트
}
