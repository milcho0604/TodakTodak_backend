package com.padaks.todaktodak.hospitaloperatinghours.controller;

import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.hospitaloperatinghours.dto.HospitalOperatingHoursReqDto;
import com.padaks.todaktodak.hospitaloperatinghours.dto.HospitalOperatingHoursResDto;
import com.padaks.todaktodak.hospitaloperatinghours.service.HospitalOperatingHoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hospital-operating-hours")
public class HospitalOperatingHoursController {
    private final HospitalOperatingHoursService hospitalOperatingHoursService;

    // 병원admin, 개발자admin만 가능
    // 병원 영업시간 등록
    @PostMapping("/register/{hospitalId}")
    public ResponseEntity<Object> addOperatingHours(@PathVariable Long hospitalId,
                                                    @RequestBody List<HospitalOperatingHoursReqDto> operatingHoursDtos) {
        hospitalOperatingHoursService.addOperatingHours(hospitalId, operatingHoursDtos);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원영업시간 등록 성공", null), HttpStatus.OK);
    }

    // 병원 영업시간 리스트 조회
    @GetMapping("/detail/{hospitalId}")
    public ResponseEntity<Object> getOperatingHours(@PathVariable Long hospitalId){
        List<HospitalOperatingHoursResDto> operatingHoursList = hospitalOperatingHoursService.getOperatingHoursByHospitalId(hospitalId);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원영업시간 조회 성공", operatingHoursList),HttpStatus.OK);
    }

    // 병원 특정 영업시간 수정
    @PostMapping("/update/{hospitalId}/{operatingHoursId}")
    public ResponseEntity<Object> updateOperatingHours(@PathVariable Long hospitalId,
                                                       @PathVariable Long operatingHoursId,
                                                       @RequestBody HospitalOperatingHoursReqDto dto) {

        hospitalOperatingHoursService.updateOperatingHours(hospitalId, operatingHoursId, dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원영업시간 수정 성공", null), HttpStatus.OK);
    }

    // 병원 특정 영업시간 삭제
    @DeleteMapping("/delete/{operatingHoursId}")
    public ResponseEntity<Object> deleteOperatingHours(@PathVariable Long operatingHoursId){
        hospitalOperatingHoursService.deleteOperatingHours(operatingHoursId);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원영업시간 삭제 성공", null), HttpStatus.OK);
    }

}
