package com.padaks.todaktodak.review.service;

import com.padaks.todaktodak.common.dto.DtoMapper;
import com.padaks.todaktodak.common.dto.MemberFeignDto;
import com.padaks.todaktodak.common.feign.MemberFeignClient;
import com.padaks.todaktodak.hospital.domain.Hospital;
import com.padaks.todaktodak.hospital.repository.HospitalRepository;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.repository.ReservationRepository;
import com.padaks.todaktodak.review.domain.Review;
import com.padaks.todaktodak.review.dto.ReviewDetailDto;
import com.padaks.todaktodak.review.dto.ReviewListResDto;
import com.padaks.todaktodak.review.dto.ReviewSaveReqDto;
import com.padaks.todaktodak.review.dto.ReviewUpdateReqDto;
import com.padaks.todaktodak.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final DtoMapper dtoMapper;
    private final MemberFeignClient memberFeignClient;
    private final HospitalRepository hospitalRepository;

    // member 객체 리턴, 토큰 포함
    public MemberFeignDto getMemberInfo() {
        MemberFeignDto member = memberFeignClient.getMemberEmail();  // Feign Client에 토큰 추가
        return member;
    }

    // 리뷰 생성
    public Review createReview(ReviewSaveReqDto dto){
        MemberFeignDto memberFeignDto = getMemberInfo();
        String email = memberFeignDto.getMemberEmail();
        String name = memberFeignDto.getName();
        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 예약내역입니다."));
        if (reservation.getMemberEmail().equals(email)){
            return reviewRepository.save(dto.toEntity(email, reservation, name));
        } else {
            throw new RuntimeException("예약 정보가 일치하지 않습니다.");
        }
    }

    // 리뷰 리스트 (병원 ID 기준)
    public Page<ReviewListResDto> reviewListResDtos(Long hospitalId, Pageable pageable) {
        // 병원 ID에 속한 예약을 조회하여 예약 ID를 가져옴
        List<Reservation> reservations = reservationRepository.findAllByHospitalId(hospitalId);

        // 예약 목록에서 리뷰가 있는 예약만 조회하여 페이징 처리
        Page<Review> reviews = reviewRepository.findByReservationInAndDeletedAtIsNull(reservations, pageable);

        // 리뷰를 DTO로 변환하여 페이징된 결과로 반환
        Page<ReviewListResDto> reviewDtos = reviews.map(review -> review.listFromEntity());

        return reviewDtos;
    }

    // 리뷰 수정
    public void updateReview(Long id, ReviewUpdateReqDto dto){
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 리뷰입니다."));
        review.updateReview(dto);
        reviewRepository.save(review);
    }

    // 리뷰 삭제
    public void deletedReview(Long id){
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 리뷰입니다."));
        review.updateDeleteAt();
        reviewRepository.save(review);
    }

    // 병원의 리뷰 점수 평균과 평점별 리뷰 개수를 계산
    public ReviewDetailDto reviewDetail(Long hospitalId, Pageable pageable) {
        // 병원에 속한 모든 예약을 조회
        List<Reservation> reservations = reservationRepository.findAllByHospitalId(hospitalId);

        // 예약 목록에서 리뷰를 페이징 처리하여 조회
        Page<Review> reviewPage = reviewRepository.findByReservationInAndDeletedAtIsNull(reservations, pageable);

        // 리뷰가 없으면 기본값 반환
        if (reviewPage.isEmpty()) {
            return new ReviewDetailDto(0.0, 0, 0, 0, 0, 0);
        }

        // 리뷰의 점수를 합산하고 평균을 계산 (소수점 첫째 자리까지 표시)
        double averageRating = reviewPage.getContent().stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

// 소수점 첫째 자리까지만 유지
        averageRating = Math.round(averageRating * 10.0) / 10.0;

        // 평점별 리뷰 개수 계산 (1점부터 5점까지)
        long count1Star = reviewPage.getContent().stream().filter(review -> review.getRating() == 1).count();
        long count2Stars = reviewPage.getContent().stream().filter(review -> review.getRating() == 2).count();
        long count3Stars = reviewPage.getContent().stream().filter(review -> review.getRating() == 3).count();
        long count4Stars = reviewPage.getContent().stream().filter(review -> review.getRating() == 4).count();
        long count5Stars = reviewPage.getContent().stream().filter(review -> review.getRating() == 5).count();

        // 결과를 DTO로 반환
        return new ReviewDetailDto(averageRating, count1Star, count2Stars, count3Stars, count4Stars, count5Stars);
    }
}
