package com.padaks.todaktodak.reservation.repository;

import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.domain.ReserveType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.*;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Page<Reservation> findByMemberEmail(Pageable pageable, String email);
    Page<Reservation> findByMemberEmailAndReservationType(Pageable pageable, String email, ReserveType reserveType);
    Optional<Reservation> findByDoctorEmailAndReservationDateAndReservationTime
            (String doctorEmail, LocalDate reservationDate, LocalTime reservationTime);
}
