package com.padaks.todaktodak.reservation.controller;

import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.dto.ReservationSaveReqDto;
import com.padaks.todaktodak.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
@Slf4j
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/scheduled")
    public ResponseEntity<?> treatScheduledReservation(@RequestBody ReservationSaveReqDto dto){
        Reservation reservation = reservationService.scheduleReservation(dto);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @GetMapping("/immediate")
    public ResponseEntity<?> treatImmediateReservation(@RequestBody ReservationSaveReqDto dto){
        Reservation reservation = reservationService.immediateReservation(dto);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<?> cancelledReservation(@PathVariable Long id){
        reservationService.cancelledReservation(id);

        return new ResponseEntity<>("취소 완료", HttpStatus.OK);
    }

}
