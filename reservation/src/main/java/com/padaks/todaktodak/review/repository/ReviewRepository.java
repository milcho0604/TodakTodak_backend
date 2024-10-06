package com.padaks.todaktodak.review.repository;

import com.padaks.todaktodak.common.dto.MemberFeignDto;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 병원별 리뷰 평균평점 (HospitalService list조회 api에서 씀)
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reservation.hospital.id = :hospitalId")
    Double findAverageRatingByHospitalId(@Param("hospitalId") Long hospitalId);

    // 병원별 리뷰 갯수
    @Query("SELECT COUNT(r) FROM Review r WHERE r.reservation.hospital.id = :hospitalId")
    Long countByHospitalId(@Param("hospitalId") Long hospitalId);


    Page<Review> findByReservationInAndDeletedAtIsNull(List<Reservation> reservations, Pageable pageable);
    boolean existsByReservationId(Long reservationId);

    // 나의 리뷰 목록
    Page<Review> findByMemberEmail(String memberEmail, Pageable pageable);

}
