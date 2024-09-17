package com.padaks.todaktodak.reservation.service;

import com.padaks.todaktodak.common.dto.DtoMapper;
import com.padaks.todaktodak.common.exception.BaseException;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.domain.ReservationHistory;
import com.padaks.todaktodak.reservation.domain.Status;
import com.padaks.todaktodak.reservation.dto.ReservationSaveReqDto;
import com.padaks.todaktodak.reservation.repository.ReservationHistoryRepository;
import com.padaks.todaktodak.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static com.padaks.todaktodak.common.exception.exceptionType.ReservationExceptionType.*;
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationHistoryRepository reservationHistoryRepository;
    private final DtoMapper dtoMapper;

//    진료 미리 예약 기능
    public Reservation scheduleReservation(ReservationSaveReqDto dto){
        log.info("ReservationService[scheduleReservation] : 시작");
//        진료 예약 시 해당 의사 선생님의 예약이 존재할 경우 Exception을 발생 시키기 위한 코드
        reservationRepository.findByDoctorEmailAndReservationDateAndReservationTime
                (dto.getDoctorEmail(), dto.getReservationDate(), dto.getReservationTime())
                .ifPresent(reservation -> {
                    throw new BaseException(RESERVATION_DUPLICATE);
                });
        Reservation reservation = dtoMapper.toReservation(dto);
        return reservationRepository.save(reservation);
    }

//    당일 진료 예약 기능 구현.
    public Reservation immediateReservation(ReservationSaveReqDto dto){
        log.info("ReservationSErvice[immediateReservation] : 시작");
        Reservation reservation = dtoMapper.toReservation(dto);
        return reservationRepository.save(reservation);
    }

//    예약 취소 기능
    public void cancelledReservation(Long id){
        log.info("ReservationSErvice[cancelledRservation] : 시작");
//        예약의 id 로 찾고 만약 예약이 없을경우 RESERVATION_NOT_FOUND 예외를 발생 -> BaseException 에 정의
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new BaseException(RESERVATION_NOT_FOUND));
//        hard delete 로 DB 상에서 완전히 지워버림
        reservationRepository.delete(reservation);
//        reservationHistory 테이블에 저장하기 위한 코드
        ReservationHistory reservationHistory = dtoMapper.toReservationHistory(reservation);
        reservationHistory.setStatus(Status.Cancelled);
//        reservationHistory 테이블에 저장.
        reservationHistoryRepository.save(reservationHistory);
    }
}
