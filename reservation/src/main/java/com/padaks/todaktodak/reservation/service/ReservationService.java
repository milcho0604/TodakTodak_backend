package com.padaks.todaktodak.reservation.service;

import com.padaks.todaktodak.common.dto.DtoMapper;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.dto.ReservationSaveReqDto;
import com.padaks.todaktodak.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DtoMapper dtoMapper;

    public Reservation scheduleReservation(ReservationSaveReqDto dto){
        log.info("ReservationService[scheduleReservation] : 시작");

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
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 예약 내역 X "));
        reservationRepository.delete(reservation);
    }
}
