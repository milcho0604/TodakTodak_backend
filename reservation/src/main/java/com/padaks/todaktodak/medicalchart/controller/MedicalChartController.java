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
@RequestMapping("/medical-chart")
public class MedicalChartController {
    private final MedicalChartService medicalChartService;
    @PostMapping("/create")
    public ResponseEntity<CommonResDto> createMedicalChart(@RequestBody MedicalChartSaveReqDto dto) {
        MedicalChartResDto medicalChartResDto = medicalChartService.medicalChartCreate(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED,"진료 내역 등록 성공", medicalChartResDto),HttpStatus.CREATED);
    }
    @GetMapping("/{reservationId}")
    public ResponseEntity<CommonResDto> createMedicalChart(@PathVariable Long reservationId) {
        MedicalChartResDto medicalChartResDto = medicalChartService.getMedicalChartByReservationId(reservationId);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"진료 내역 조회 성공", medicalChartResDto),HttpStatus.OK);
    }
    @PostMapping("/{medicalChartId}/complete")
    public ResponseEntity<CommonResDto> completeMedicalChart(@PathVariable Long medicalChartId) {
        MedicalChartResDto medicalChartResDto = medicalChartService.completeMedicalChart(medicalChartId);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"진료 완료 처리 성공", medicalChartResDto),HttpStatus.OK);
    }
    @PostMapping("/{medicalChartId}/pay")
    public ResponseEntity<CommonResDto> payMedicalChart(@PathVariable Long medicalChartId) {
        MedicalChartResDto medicalChartResDto = medicalChartService.payMedicalChart(medicalChartId);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK,"결제 완료 처리 성공", medicalChartResDto),HttpStatus.OK);
    }
}