package com.padaks.todaktodak.hospitaloperatinghours.repository;

import com.padaks.todaktodak.hospitaloperatinghours.domain.HospitalOperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalOperatingHoursRepository extends JpaRepository<HospitalOperatingHours, Long> {
}
