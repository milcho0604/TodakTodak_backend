package com.padaks.todaktodak;

import com.padaks.todaktodak.common.dto.DtoMapper;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.repository.ReservationRepository;
import com.padaks.todaktodak.review.domain.Review;
import com.padaks.todaktodak.review.repository.ReviewRepository;
import com.padaks.todaktodak.review.dto.CreateReviewReqDto;
import com.padaks.todaktodak.review.service.ReviewService;
import com.padaks.todaktodak.common.exception.BaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.padaks.todaktodak.common.exception.exceptionType.ReservationExceptionType.RESERVATION_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ReviewTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private DtoMapper dtoMapper;

    @InjectMocks
    private ReviewService reviewService; // 테스트 대상 서비스 클래스

    private CreateReviewReqDto createReviewReqDto;
    private Reservation reservation;
    private Review review;

    @BeforeEach
    void setUp() {
        // 테스트에 필요한 객체 초기화
        createReviewReqDto = CreateReviewReqDto.builder()
                .id(1L)
                .rating(5)
                .contents("Great service!")
                .build();

        System.out.println(createReviewReqDto.toString());
        reservation = Reservation.builder()
                .id(1L)
                .memberEmail("test@example.com")
                .doctorEmail("doctor@example.com")
                .build();

        System.out.println(reservation);

        review = Review.builder()
                .id(1L)
                .rating(5)
                .contents("Great service!")
                .reservation(reservation)
                .build();

        System.out.println(review);
    }

    @Test
    void testReviewCreateSuccess() {
        // Reservation이 존재하는 경우
        when(reservationRepository.findById(createReviewReqDto.getId()))
                .thenReturn(Optional.of(reservation));

        // DtoMapper가 Review 객체로 변환
        when(dtoMapper.toReview(createReviewReqDto, reservation)).thenReturn(review);

        // ReviewRepository에 저장
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // 서비스 메서드 실행
        assertDoesNotThrow(() -> reviewService.reviewCreate(createReviewReqDto));

        // 검증
        verify(reservationRepository, times(1)).findById(createReviewReqDto.getId());
        verify(dtoMapper, times(1)).toReview(createReviewReqDto, reservation);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testReviewCreateReservationNotFound() {
        // Reservation이 존재하지 않는 경우
        when(reservationRepository.findById(createReviewReqDto.getId()))
                .thenReturn(Optional.empty());

        // 예외 발생을 확인
        BaseException exception = assertThrows(BaseException.class, () -> {
            reviewService.reviewCreate(createReviewReqDto);
        });

        assertEquals(RESERVATION_NOT_FOUND.message(), exception.getMessage());

        // 검증
        verify(reservationRepository, times(1)).findById(createReviewReqDto.getId());
        verify(dtoMapper, times(0)).toReview(any(), any());
        verify(reviewRepository, times(0)).save(any());
    }
}
