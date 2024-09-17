package com.padaks.todaktodak.hospital.controller;

import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.hospital.domain.Hospital;
import com.padaks.todaktodak.hospital.dto.HospitalDTO.HospitalDetailResDto;
import com.padaks.todaktodak.hospital.dto.HospitalDTO.HospitalRegisterReqDto;
import com.padaks.todaktodak.hospital.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hospital")
public class HospitalController {
    private final HospitalService hospitalService;

    // 병원생성 (병원 admin만 가능) 이후 주석해제 예정
//    @PreAuthorize("hasRole('ROLE_HOSPTIALADMIN')")
    @PostMapping("/register")
    public ResponseEntity<Object> registerHospital(@ModelAttribute HospitalRegisterReqDto hospitalRegisterReqDto){
        Hospital hospital = hospitalService.hospitalRegister(hospitalRegisterReqDto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "병원등록성공", hospital.getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<HospitalDetailResDto> getHospitalDetail(
            @PathVariable Long id,
            @RequestParam BigDecimal latitude,
            @RequestParam BigDecimal longitude) {

        HospitalDetailResDto hospitalDetail = hospitalService.getHospitalDetail(id, latitude, longitude);
        return ResponseEntity.ok(hospitalDetail);
    }

    // TODO : 병원-의사, 병원-운영시간 조회api



}
