package com.padaks.todaktodak.hospital.controller;

import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.hospital.domain.Hospital;
import com.padaks.todaktodak.hospital.dto.HospitalRegisterReqDto;
import com.padaks.todaktodak.hospital.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hospital")
public class HospitalController {
    private final HospitalService hospitalService;

    // 병원생성 (병원 admin만 가능)
//    @PreAuthorize("hasRole('ROLE_HOSPTIALADMIN')")
    @PostMapping("/register")
    public ResponseEntity<Object> registerHospital(@ModelAttribute HospitalRegisterReqDto hospitalRegisterReqDto){
        Hospital hospital = hospitalService.hospitalRegister(hospitalRegisterReqDto);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "병원등록성공", hospital.getId());
        return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
    }
}
