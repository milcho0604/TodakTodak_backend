package com.padaks.todaktodak.hospitaloperatinghours.service;

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

import java.util.List;
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
        for(HospitalOperatingHoursReqDto dto : operatingHoursDtos){
            HospitalOperatingHours operatingHours = HospitalOperatingHoursReqDto.toEntity(hospital, dto);
            hospitalOperatingHoursRepository.save(operatingHours);
        }
    }

    // 특정병원의 모든 영업시간 리스트 조회
    public List<HospitalOperatingHoursResDto> getOperatingHoursByHospitalId(Long hospitalId) {
        List<HospitalOperatingHours> operatingHoursList = hospitalOperatingHoursRepository.findByHospitalId(hospitalId);

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
