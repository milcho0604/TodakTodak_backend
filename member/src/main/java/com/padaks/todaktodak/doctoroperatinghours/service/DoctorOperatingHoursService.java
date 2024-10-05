package com.padaks.todaktodak.doctoroperatinghours.service;

import com.padaks.todaktodak.common.enumdir.DayOfHoliday;
import com.padaks.todaktodak.doctoroperatinghours.domain.DoctorOperatingHours;
import com.padaks.todaktodak.doctoroperatinghours.dto.DoctorOperatingHoursReqDto;
import com.padaks.todaktodak.doctoroperatinghours.dto.DoctorOperatingHoursSimpleResDto;
import com.padaks.todaktodak.doctoroperatinghours.repository.DoctorOperatingHoursRepository;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DoctorOperatingHoursService {
    private final DoctorOperatingHoursRepository doctorOperatingHoursRepository;
    private final MemberRepository memberRepository;

    public void addOperatingHours(Long doctorId, List<DoctorOperatingHoursReqDto> dtos){
        Member doctor = memberRepository.findByIdOrThrow(doctorId);

        //의사의 모든 영업시간 불러옴
        List<DoctorOperatingHours> existingOperatingHours = doctorOperatingHoursRepository.findAllByMember(doctor);

        //기존 의사 영업시간 요일 저장
        Set<DayOfHoliday> existingDays = existingOperatingHours.stream()
                .map(DoctorOperatingHours::getDayOfWeek)
                .collect(Collectors.toSet());

        for (DoctorOperatingHoursReqDto dto : dtos){
            //중복 저장 방지
            if (existingDays.contains(dto.getDayOfWeek())){
                throw new IllegalArgumentException("근무시간 중복 저장 불가 : "+dto.getDayOfWeek() +"요일에 영업시간이 이미 등록 되어 있습니다.");
            }
            DoctorOperatingHours operatingHours = DoctorOperatingHoursReqDto.toEntity(doctor, dto);
            doctorOperatingHoursRepository.save(operatingHours);
        }
    }

    public List<DoctorOperatingHoursSimpleResDto> getOperatingHoursByDoctorId(Long doctorId){
        List<DoctorOperatingHours> operatingHoursList = doctorOperatingHoursRepository.findByMemberId(doctorId);
        return operatingHoursList.stream()
                .map(hours -> new DoctorOperatingHoursSimpleResDto(
                        hours.getId(),
                        hours.getMember().getName(),
                        hours.getDayOfWeek(),
                        hours.getOpenTime(),
                        hours.getCloseTime())
                ).collect(Collectors.toList());
    }

    public void updateOperatingHours(Long doctorId, Long operatingHoursId, DoctorOperatingHoursReqDto dto){

        Member doctor = memberRepository.findByIdOrThrow(doctorId);
        DoctorOperatingHours hours = doctorOperatingHoursRepository.findByIdOrThrow(operatingHoursId);

        if (!hours.getMember().getId().equals(doctorId)){
            throw new EntityNotFoundException("해당하는 의사와 동일 하지 않습니다.");
        }hours.updateOperatingHours(dto);
    }

    public void deleteOperatingHours(Long operatingHoursId){
        DoctorOperatingHours hours = doctorOperatingHoursRepository.findByIdOrThrow(operatingHoursId);
        hours.setDeletedTimeAt(LocalDateTime.now());
    }
}
