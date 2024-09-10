package com.padaks.todaktodak.hospital.repository;

import com.padaks.todaktodak.hospital.domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
