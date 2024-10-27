package com.padaks.todaktodak.reservation.controller;

import com.padaks.todaktodak.reservation.domain.ReserveType;
import com.padaks.todaktodak.reservation.domain.Status;
import com.padaks.todaktodak.reservation.dto.CheckHospitalListReservationReqDto;
import com.padaks.todaktodak.reservation.dto.CheckHospitalListReservationResDto;
import com.padaks.todaktodak.reservation.dto.HospitalReservationListReqDto;

import com.padaks.todaktodak.reservation.dto.UpdateStatusReservation;
import com.padaks.todaktodak.reservation.service.ReservationAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation/hospital")
public class ReservationAdminController {

    private final ReservationAdminService reservationAdminService;

    @GetMapping("/list/untact")
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

    @PreAuthorize("hasAnyRole('HOSPITAL')")
    @GetMapping("/list")
    public ResponseEntity<?> adminImmediateReservationList(
            @RequestParam(required = false) String hospitalName,
            @RequestParam(required = false) ReserveType reserveType,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        HospitalReservationListReqDto reqDto = new HospitalReservationListReqDto(hospitalName, reserveType, status, date);
        List<?> dto = reservationAdminService.checkImmediateReservationList(reqDto);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/total/list")
    public ResponseEntity<?> totalReservationList(){
        return new ResponseEntity<>(reservationAdminService.totalReservationList(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('HOSPITAL')")
    @PostMapping("/update")
    public ResponseEntity<?> updateStatusReservation(
            @RequestBody UpdateStatusReservation updateStatusReservation){
        reservationAdminService.statusReservation(updateStatusReservation);

        return new ResponseEntity<>("수정 완료", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('HOSPITAL','DOCTOR')")
    @PostMapping("/untact/update")
    public ResponseEntity<?> updateStatusUntactReservation(
            @RequestBody UpdateStatusReservation updateStatusReservation){
        reservationAdminService.updateStatusUntactReservation(updateStatusReservation);

        return new ResponseEntity<>("수정 완료", HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('HOSPITAL', 'DOCTOR')")
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
