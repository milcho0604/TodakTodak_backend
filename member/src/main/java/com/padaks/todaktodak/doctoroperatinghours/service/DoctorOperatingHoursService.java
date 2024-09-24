package com.padaks.todaktodak.doctoroperatinghours.service;

import com.padaks.todaktodak.doctoroperatinghours.domain.DoctorOperatingHours;
import com.padaks.todaktodak.doctoroperatinghours.dto.DoctorOperatingHoursReqDto;
import com.padaks.todaktodak.doctoroperatinghours.repository.DoctorOperatingHoursRepository;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DoctorOperatingHoursService {
    private final DoctorOperatingHoursRepository doctorOperatingHoursRepository;
    private final MemberRepository memberRepository;

    public void addOperatingHours(Long doctorId, List<DoctorOperatingHoursReqDto> dtos){
        Member doctor = memberRepository.findByIdOrThrow(doctorId);
        for (DoctorOperatingHoursReqDto dto : dtos){
            DoctorOperatingHours operatingHours = DoctorOperatingHoursReqDto.toEntity(doctor, dto);
            doctorOperatingHoursRepository.save(operatingHours);
        }
    }
}
