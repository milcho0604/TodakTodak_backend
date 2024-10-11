package com.padaks.todaktodak.reservation.controller;

import com.padaks.todaktodak.reservation.dto.CheckHospitalListReservationReqDto;
import com.padaks.todaktodak.reservation.dto.CheckHospitalListReservationResDto;
import com.padaks.todaktodak.reservation.dto.UpdateStatusReservation;
import com.padaks.todaktodak.reservation.service.ReservationAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation/hospital")
public class ReservationAdminController {

    private final ReservationAdminService reservationAdminService;

    @GetMapping("/list")
    public ResponseEntity<?> adminListReservation(
            @RequestBody CheckHospitalListReservationReqDto reqDto,
            Pageable pageable){
        List<?> dto = reservationAdminService.checkListReservation(reqDto, pageable);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateStatusReservation(
            @RequestBody UpdateStatusReservation updateStatusReservation){
        reservationAdminService.statusReservation(updateStatusReservation);

        return new ResponseEntity<>("수정 완료", HttpStatus.OK);
    }
}
