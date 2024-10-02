package com.padaks.todaktodak.review.repository;

import com.padaks.todaktodak.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 예약 ID로 리뷰를 조회
//    Optional<Review> findByReservationId(Long reservationId);

    Optional<Review> findByReservationIdAndDeletedAtIsNull(Long reservationId);

}
