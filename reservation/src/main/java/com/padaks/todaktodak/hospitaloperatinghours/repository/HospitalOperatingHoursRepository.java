package com.padaks.todaktodak.hospitaloperatinghours.repository;

import com.padaks.todaktodak.common.exception.BaseException;
import com.padaks.todaktodak.hospital.domain.Hospital;
import com.padaks.todaktodak.hospitaloperatinghours.domain.HospitalOperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.padaks.todaktodak.common.exception.exceptionType.HospitalOperatingHoursExceptionType.HOSPITAL_OPERATING_HOURS_NOT_FOUND;

@Repository
public interface HospitalOperatingHoursRepository extends JpaRepository<HospitalOperatingHours, Long> {

    // 병원id로 영업시간 리스트 조회
    List<HospitalOperatingHours> findByHospitalId(Long hospitalId);

    // deleteAt이 null인 객체 찾음(삭제되지 않은 객체)
    Optional<HospitalOperatingHours> findByIdAndDeletedAtIsNull(Long id);

    // 삭제되지 않은 객체 중 id로 찾음
    default HospitalOperatingHours findByIdOrThrow(Long id) {
        return findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new BaseException(HOSPITAL_OPERATING_HOURS_NOT_FOUND));
    }

    // 병원에 속한 모든 영업시간을 가져옴
    List<HospitalOperatingHours> findAllByHospital(Hospital hospital);

}
