package com.padaks.todaktodak.medicalchart.repository;

import com.padaks.todaktodak.chatmessage.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalChartRepository extends JpaRepository<MedicalChart, Long> {
    Optional<MedicalChart> findByReservation(Reservation reservation);
}
