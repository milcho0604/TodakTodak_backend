package com.padaks.todaktodak.reservation.repository;

import com.padaks.todaktodak.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.*;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByDoctorEmailAndReservationDateAndReservationTime
            (String doctorEmail, LocalDate reservationDate, LocalTime reservationTime);
}
