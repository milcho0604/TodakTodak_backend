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
        reservation = Reservation.builder()
                .id(1L)
                .memberEmail("test@example.com")
                .doctorEmail("doctor@example.com")
                .build();

        review = Review.builder()
                .id(1L)
                .rating(5)
                .contents("Great service!")
                .reservation(reservation)
                .build();

    }

    @Test
    void testReviewCreateSuccess() {
        System.out.println("testReviewCreateSuccess: 시작");
        // Reservation이 존재하는 경우
//        동일 한 메서드에 대해 두 개의 when 이 있을 경우, 나중에 쓴 when이 이전 상황을 덮어 쓰기함
//        그래서 when 메서드는 두번 실행 되었지만 findById 는 한번만 실행된 것으로 판단됨.
        when(reservationRepository.findById(createReviewReqDto.getId()))
                .thenReturn(Optional.of(reservation));
        when(reservationRepository.findById(createReviewReqDto.getId()))
                .thenReturn(Optional.of(reservation));

        // DtoMapper를 통해 Review 객체로 변환 -> 이 코드 해결 하려고 작성했음..... 으아악 
        when(dtoMapper.toReview(createReviewReqDto, reservation)).thenReturn(review);

        // ReviewRepository를 통해 DB에 저장함.
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // 서비스 메서드를 통해 reviewCreate 로직 수행
        assertDoesNotThrow(() -> reviewService.reviewCreate(createReviewReqDto));

        // Mockito를 사용해 작성한 테스트에서 특정 메서드가 예상되로 호출되었는지 테스트 하는 코드
//        reservationRepository 가 1번 실행되었는지 확인하는 메서드
        verify(reservationRepository, times(1)).findById(createReviewReqDto.getId());
        verify(dtoMapper, times(1)).toReview(createReviewReqDto, reservation);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void testReviewCreateReservationNotFound() {
        System.out.println("testReviewCreateReservationNotFound: 시작");
        // Reservation이 존재하지 않는 경우
//        위에서 createREviewReqDto를 넣었지만 when 메서드의 Optional.empty를 통해
//        reservationRepository.findById 가 호출된 다면 아무 값도 반환하지 않겠다는 소리.
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
