package com.padaks.todaktodak.hospital.controller;

import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.hospital.domain.Hospital;
import com.padaks.todaktodak.hospital.dto.HospitalDTO.HospitalDetailResDto;
import com.padaks.todaktodak.hospital.dto.HospitalDTO.HospitalRegisterReqDto;
import com.padaks.todaktodak.hospital.dto.HospitalDTO.HospitalUpdateReqDto;
import com.padaks.todaktodak.hospital.dto.HospitalDTO.HospitalUpdateResDto;
import com.padaks.todaktodak.hospital.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hospital")
public class HospitalController {
    private final HospitalService hospitalService;

    // 병원생성 (병원 admin만 가능) 이후 주석해제 예정
//    @PreAuthorize("hasRole('ROLE_HOSPTIALADMIN')")
    @PostMapping("/register")
    public ResponseEntity<Object> registerHospital(@ModelAttribute HospitalRegisterReqDto hospitalRegisterReqDto){
        Hospital hospital = hospitalService.hospitalRegister(hospitalRegisterReqDto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED, "병원등록성공", hospital.getId()), HttpStatus.CREATED);
    }

    // 병원 detail 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<Object> getHospitalDetail(@PathVariable Long id,
                                                    @RequestParam BigDecimal latitude,
                                                    @RequestParam BigDecimal longitude) {

        HospitalDetailResDto hospitalDetail = hospitalService.getHospitalDetail(id, latitude, longitude);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원정보 조회성공", hospitalDetail), HttpStatus.OK);
    }

    // TODO : 병원-의사, 병원-운영시간 조회api

    // 병원정보 수정
    @PostMapping("/update")
    public ResponseEntity<Object> updateHospital(@ModelAttribute HospitalUpdateReqDto hospitalUpdateReqDto){
        HospitalUpdateResDto hospitalUpdateResDto = hospitalService.updateHospital(hospitalUpdateReqDto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원정보 수정성공", hospitalUpdateResDto), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteHospital(@PathVariable Long id){
        hospitalService.deleteHospital(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원 삭제성공", null), HttpStatus.OK);
    }
}
