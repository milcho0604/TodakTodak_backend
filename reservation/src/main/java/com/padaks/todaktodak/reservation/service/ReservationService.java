package com.padaks.todaktodak.reservation.service;

import com.padaks.todaktodak.common.dto.DtoMapper;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.dto.CheckListReservationReqDto;
import com.padaks.todaktodak.reservation.dto.ReservationSaveReqDto;
import com.padaks.todaktodak.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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
    
//    예약 조회 기능
    public List<?> checkListReservation(String email, Pageable pageable){
//        feign 으로 연결 되면 여기에 email 로 해당 user 찾는 로직이 들어갈 예정
        
//        여기서 페이징 처리할 예정 -> 페이징 처리하면서 예약
//        여기서 미리 예약 , 당일 예약 분기처리도 해줄 예정
        Page<Reservation> reservationPage = reservationRepository.findByMemberEmail(pageable, email);

        List<CheckListReservationReqDto> dto = reservationPage.stream()
                .map(dtoMapper::toListReservation)
                .collect(Collectors.toList());

        return dto;
    }
}
