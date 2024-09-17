package com.padaks.todaktodak.reservation.repository;

import com.padaks.todaktodak.reservation.domain.ReservationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, Long> {
}
