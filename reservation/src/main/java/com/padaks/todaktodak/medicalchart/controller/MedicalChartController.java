package com.padaks.todaktodak.medicalchart.controller;

import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.medicalchart.dto.MedicalChartResDto;
import com.padaks.todaktodak.medicalchart.dto.MedicalChartSaveReqDto;
import com.padaks.todaktodak.medicalchart.service.MedicalChartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MedicalChartController {
    private final MedicalChartService medicalChartService;
    @PostMapping("/medical-chart/create")
    public ResponseEntity<CommonResDto> createMedicalChart(@RequestBody MedicalChartSaveReqDto dto) {
        MedicalChartResDto medicalChartResDto = medicalChartService.medicalChartCreate(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED,"진료 내역 등록 성공", medicalChartResDto),HttpStatus.CREATED);
    }
    @GetMapping("/medical-chart/{reservationId}")
    public ResponseEntity<CommonResDto> createMedicalChart(@PathVariable Long reservationId) {
        MedicalChartResDto medicalChartResDto = medicalChartService.getMedicalChartByReservationId(reservationId);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED,"진료 내역 조회 성공", medicalChartResDto),HttpStatus.CREATED);
    }
}