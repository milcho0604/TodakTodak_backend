package com.padaks.todaktodak.doctor.repository;

import com.padaks.todaktodak.doctor.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
