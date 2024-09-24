package com.padaks.todaktodak.doctoroperatinghours.repository;

import com.padaks.todaktodak.common.exception.BaseException;
import com.padaks.todaktodak.doctoroperatinghours.domain.DoctorOperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorOperatingHoursRepository extends JpaRepository<DoctorOperatingHours, Long> {
    List<DoctorOperatingHours> findByMemberId(Long memberId);
    Optional<DoctorOperatingHours> findByIdAndDeletedAtIsNull(Long id);

    default DoctorOperatingHours findByIdOrThrow(Long id) {
        return findByIdAndDeletedAtIsNull(id).orElseThrow(()-> new EntityNotFoundException("해당하는 의사가 없습니다."));
    }
}
