package com.padaks.todaktodak.reservation.controller;

import com.padaks.todaktodak.reservation.domain.Status;
import com.padaks.todaktodak.reservation.dto.CheckHospitalListReservationReqDto;
import com.padaks.todaktodak.reservation.dto.CheckHospitalListReservationResDto;
import com.padaks.todaktodak.reservation.dto.UpdateStatusReservation;
import com.padaks.todaktodak.reservation.service.ReservationAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation/hospital")
public class ReservationAdminController {

    private final ReservationAdminService reservationAdminService;

    @GetMapping("/list")
    public ResponseEntity<?> adminListReservation(
            @RequestParam(required = false) String memberEmail,
            @RequestParam(required = false) String doctorEmail,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Pageable pageable){
        CheckHospitalListReservationReqDto reqDto = new CheckHospitalListReservationReqDto(memberEmail, doctorEmail, status, date);
        List<?> dto = reservationAdminService.checkListReservation(reqDto, pageable);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateStatusReservation(
            @RequestBody UpdateStatusReservation updateStatusReservation){
        reservationAdminService.statusReservation(updateStatusReservation);

        return new ResponseEntity<>("수정 완료", HttpStatus.OK);
    }

    @GetMapping("/doctor/list")
    public ResponseEntity<?> ListReservation(
            @RequestParam(required = false) String doctorEmail,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) boolean untact,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
        ){
        List<CheckHospitalListReservationResDto> dto = reservationAdminService.getDoctorReservation(doctorEmail, status, untact, date);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
