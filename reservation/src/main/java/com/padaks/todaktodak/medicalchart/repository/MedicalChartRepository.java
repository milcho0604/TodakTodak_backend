package com.padaks.todaktodak.medicalchart.repository;

import com.padaks.todaktodak.medicalchart.domain.MedicalChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalChartRepository extends JpaRepository<MedicalChart, Long> {
}
