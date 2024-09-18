package com.padaks.todaktodak.review.service;

import com.padaks.todaktodak.common.dto.DtoMapper;
import com.padaks.todaktodak.common.exception.BaseException;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.repository.ReservationRepository;
import com.padaks.todaktodak.review.domain.Review;
import com.padaks.todaktodak.review.dto.CreateReviewReqDto;
import com.padaks.todaktodak.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.padaks.todaktodak.common.exception.exceptionType.ReservationExceptionType.RESERVATION_NOT_FOUND;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final DtoMapper dtoMapper;

    public void reviewCreate(CreateReviewReqDto dto){
        Reservation reservation = reservationRepository.findById(dto.getId())
                .orElseThrow(() -> new BaseException(RESERVATION_NOT_FOUND));

        Review review = reviewRepository.save(dtoMapper.toReview(dto, reservation));
        log.info(String.valueOf(review.getReservation()));
    }
}
