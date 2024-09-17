package com.padaks.todaktodak.reservation.controller;

import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.dto.CheckListReservationResDto;
import com.padaks.todaktodak.reservation.dto.ReservationSaveReqDto;
import com.padaks.todaktodak.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

//    rest 에서 조회할 때는 @PathVariable 로 email 로 조회하겠음
//    추후에 email 로 해도 되고 id 로 해도 될 듯?
    @GetMapping("/list")
    public ResponseEntity<?> listReservation(@RequestBody CheckListReservationResDto resDto, Pageable pageable){
        List<?> dto = reservationService.checkListReservation(resDto, pageable);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
