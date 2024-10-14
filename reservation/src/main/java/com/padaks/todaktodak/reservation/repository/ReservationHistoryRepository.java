package com.padaks.todaktodak.reservation.repository;

import com.padaks.todaktodak.reservation.domain.ReservationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, Long> {

    List<ReservationHistory> findByMemberEmailAndReservationDateGreaterThanEqual(String memberEmail, LocalDate localDate);
    List<ReservationHistory> findByMemberEmailAndReservationDateBefore(String memberEmail, LocalDate localDate);
}
