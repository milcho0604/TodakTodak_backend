package com.padaks.todaktodak.hospitaloperatinghours.service;

import com.padaks.todaktodak.common.enumdir.DayOfHoliday;
import com.padaks.todaktodak.common.exception.BaseException;
import com.padaks.todaktodak.hospital.domain.Hospital;
import com.padaks.todaktodak.hospital.repository.HospitalRepository;
import com.padaks.todaktodak.hospitaloperatinghours.domain.HospitalOperatingHours;
import com.padaks.todaktodak.hospitaloperatinghours.dto.HospitalOperatingHoursReqDto;
import com.padaks.todaktodak.hospitaloperatinghours.dto.HospitalOperatingHoursResDto;
import com.padaks.todaktodak.hospitaloperatinghours.repository.HospitalOperatingHoursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.padaks.todaktodak.common.exception.exceptionType.HospitalOperatingHoursExceptionType.MISMATCHED_HOSPITAL;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class HospitalOperatingHoursService {
    private final HospitalOperatingHoursRepository hospitalOperatingHoursRepository;
    private final HospitalRepository hospitalRepository;

    // 특정 병원에 영업시간 추가
    public void addOperatingHours(List<HospitalOperatingHoursReqDto> operatingHoursDtos){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String adminEmail = userDetails.getUsername();
        // 병원이 존재하는지 확인
        Hospital hospital = hospitalRepository.findByAdminEmail(adminEmail)
                .orElseThrow(() -> new EntityNotFoundException("해당 병원의 관리자가 아닙니다."));
        // 해당 병원의 영업시간을 모두 불러옴
        List<HospitalOperatingHours> existingOperatingHours = hospitalOperatingHoursRepository.findAllByHospital(hospital);

        // 요일별로 영업시간이 존재하는지 미리 체크
        Set<DayOfHoliday> existingDays = existingOperatingHours.stream()
                .map(HospitalOperatingHours::getDayOfWeek)
                .collect(Collectors.toSet());

        for (HospitalOperatingHoursReqDto dto : operatingHoursDtos) {
            if (existingDays.contains(dto.getDayOfWeek())) {
                throw new IllegalArgumentException(dto.getDayOfWeek() + "에 대한 영업시간이 이미 존재합니다.");
            } else {
                HospitalOperatingHours operatingHours = HospitalOperatingHoursReqDto.toEntity(hospital, dto);
                hospitalOperatingHoursRepository.save(operatingHours);
            }
        }

    }

    // 특정병원의 모든 영업시간 리스트 조회
    public List<HospitalOperatingHoursResDto> getOperatingHoursByHospitalId(Long hospitalId) {
        List<HospitalOperatingHours> operatingHoursList = hospitalOperatingHoursRepository.findByHospitalId(hospitalId);

        return operatingHoursList.stream()
                .map(HospitalOperatingHoursResDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 병원 어드민 리스트 조회
    public List<HospitalOperatingHoursResDto> adminOperatingHoursByHospitalId(String adminEmail) {
        Hospital hospital = hospitalRepository.findByAdminEmail(adminEmail)
                .orElseThrow(() -> new EntityNotFoundException("해당 병원의 관리자가 아닙니다."));

        List<HospitalOperatingHours> operatingHoursList = hospitalOperatingHoursRepository.findByHospitalId(hospital.getId());

        return operatingHoursList.stream()
                .map(HospitalOperatingHoursResDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정병원 특정영업시간 수정
    public void updateOperatingHours(Long operatingHoursId,
                                     HospitalOperatingHoursReqDto dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String adminEmail = userDetails.getUsername();
        // 병원이 존재하는지 확인
        Hospital hospital = hospitalRepository.findByAdminEmail(adminEmail)
                .orElseThrow(() -> new EntityNotFoundException("해당 병원의 관리자가 아닙니다."));
        // 수정할 영업시간 찾기
        HospitalOperatingHours operatingHours = hospitalOperatingHoursRepository.findByIdOrThrow(operatingHoursId);

        // 병원이 일치하는지 확인
        if (!operatingHours.getHospital().getId().equals(hospital.getId())) {
            throw new BaseException(MISMATCHED_HOSPITAL);
        }

        // 해당 병원의 모든 영업시간을 불러옴
        List<HospitalOperatingHours> existingOperatingHours = hospitalOperatingHoursRepository.findAllByHospital(hospital);

        // 기존 운영 시간의 요일을 Set으로 수집
        Set<DayOfHoliday> existingDays = existingOperatingHours.stream()
                .map(HospitalOperatingHours::getDayOfWeek)
                .collect(Collectors.toSet());

        // 요청된 요일이 기존 운영 시간에 존재하는지 확인
        if (existingDays.contains(dto.getDayOfWeek()) && !operatingHours.getDayOfWeek().equals(dto.getDayOfWeek())) {
            throw new IllegalArgumentException(dto.getDayOfWeek() + "에 대한 영업시간이 이미 존재합니다.");
        }

        // 운영 시간 업데이트
        operatingHours.updateOperatingHours(dto);
    }

    // 특정병원 특정영업시간 삭제
    public void deleteOperatingHours(Long operatingHoursId){
        HospitalOperatingHours operatingHours = hospitalOperatingHoursRepository.findByIdOrThrow(operatingHoursId);
        operatingHours.updateDeleteAt();
    }

}
