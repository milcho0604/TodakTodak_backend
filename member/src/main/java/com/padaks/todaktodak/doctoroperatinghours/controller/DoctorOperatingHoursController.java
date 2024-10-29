package com.padaks.todaktodak.doctoroperatinghours.controller;

import com.padaks.todaktodak.common.dto.CommonErrorDto;
import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.doctoroperatinghours.dto.DoctorOperatingHoursReqDto;
import com.padaks.todaktodak.doctoroperatinghours.dto.DoctorOperatingHoursSimpleResDto;
import com.padaks.todaktodak.doctoroperatinghours.service.DoctorOperatingHoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//import javax.ws.rs.Path;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctor-operating-hours")
public class DoctorOperatingHoursController {
    private final DoctorOperatingHoursService doctorOperatingHoursService;

    @PreAuthorize("hasAnyRole('HOSPITAL', 'ADMIN')")
    @PostMapping("/register/{doctorId}")
    public ResponseEntity<?> addOperatingHours(@PathVariable Long doctorId, @RequestBody List<DoctorOperatingHoursReqDto> dto){
        try {
            doctorOperatingHoursService.addOperatingHours(doctorId, dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "의사 근무 시간 등록 성공", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<?> getOperatingHours(@PathVariable Long doctorId){
        List<DoctorOperatingHoursSimpleResDto> operatingHoursList = doctorOperatingHoursService.getOperatingHoursByDoctorId(doctorId);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "의사 근무시간 : ", operatingHoursList);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('HOSPITAL', 'ADMIN')")
    @PostMapping("/update/{operatingHoursId}")
    public ResponseEntity<?> updateOperatingHours(@PathVariable Long operatingHoursId, @RequestBody DoctorOperatingHoursReqDto dto){
        try{
            doctorOperatingHoursService.updateOperatingHours(operatingHoursId, dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"의사 근무시간 수정 성공", null);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAnyRole('HOSPITAL', 'ADMIN')")
    @PostMapping("/delete/{operatingHoursId}")
    public ResponseEntity<?> deleteOperatingHours(@PathVariable Long operatingHoursId){
        try{
            doctorOperatingHoursService.deleteOperatingHours(operatingHoursId);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "의사 근무시간 삭제 성공", operatingHoursId);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }

    }
}
