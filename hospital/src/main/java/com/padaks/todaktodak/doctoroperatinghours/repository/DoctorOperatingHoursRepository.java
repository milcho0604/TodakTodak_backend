package com.padaks.todaktodak.doctoroperatinghours.repository;

import com.padaks.todaktodak.doctoroperatinghours.domain.DoctorOperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorOperatingHoursRepository extends JpaRepository<DoctorOperatingHours, Long> {
}
