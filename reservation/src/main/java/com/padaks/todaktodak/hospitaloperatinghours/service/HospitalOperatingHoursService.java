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
    public void addOperatingHours(Long hospitalId,
                                  List<HospitalOperatingHoursReqDto> operatingHoursDtos){

        Hospital hospital = hospitalRepository.findByIdOrThrow(hospitalId);

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

    public List<HospitalOperatingHoursResDto> adminOperatingHoursByHospitalId(String adminEmail) {
        Hospital hospital = hospitalRepository.findByAdminEmail(adminEmail)
                .orElseThrow(() -> new EntityNotFoundException("해당 병원의 관리자가 아닙니다."));

        List<HospitalOperatingHours> operatingHoursList = hospitalOperatingHoursRepository.findByHospitalId(hospital.getId());

        return operatingHoursList.stream()
                .map(HospitalOperatingHoursResDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정병원 특정영업시간 수정
    public void updateOperatingHours(Long hospitalId,
                                     Long operatingHoursId,
                                     HospitalOperatingHoursReqDto dto){
        // 병원이 존재하는지 확인
        Hospital hospital = hospitalRepository.findByIdOrThrow(hospitalId);
        // 수정할 영업시간 찾기
        HospitalOperatingHours operatingHours = hospitalOperatingHoursRepository.findByIdOrThrow(operatingHoursId);

        // 병원이 일치하는지 확인
        if (!operatingHours.getHospital().getId().equals(hospitalId)) {
            throw new BaseException(MISMATCHED_HOSPITAL);
        }
        operatingHours.updateOperatingHours(dto);
    }

    // 특정병원 특정영업시간 삭제
    public void deleteOperatingHours(Long operatingHoursId){
        HospitalOperatingHours operatingHours = hospitalOperatingHoursRepository.findByIdOrThrow(operatingHoursId);
        operatingHours.updateDeleteAt();
    }

}
