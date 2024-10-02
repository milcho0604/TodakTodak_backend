package com.padaks.todaktodak.review.repository;

import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByReservationInAndDeletedAtIsNull(List<Reservation> reservations, Pageable pageable);
    boolean existsByReservationId(Long reservationId);
}
