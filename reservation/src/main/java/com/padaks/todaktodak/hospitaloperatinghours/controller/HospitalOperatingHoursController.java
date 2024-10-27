package com.padaks.todaktodak.hospitaloperatinghours.controller;

import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.common.exception.BaseException;
import com.padaks.todaktodak.hospitaloperatinghours.dto.HospitalOperatingHoursReqDto;
import com.padaks.todaktodak.hospitaloperatinghours.dto.HospitalOperatingHoursResDto;
import com.padaks.todaktodak.hospitaloperatinghours.service.HospitalOperatingHoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hospital-operating-hours")
public class HospitalOperatingHoursController {
    private final HospitalOperatingHoursService hospitalOperatingHoursService;

    // 병원 영업시간 등록 (병원admin만 가능)
    @PreAuthorize("hasRole('HOSPITAL')")
    @PostMapping("/register")
    public ResponseEntity<Object> addOperatingHours(@RequestBody List<HospitalOperatingHoursReqDto> operatingHoursDtos) {
        try{
            hospitalOperatingHoursService.addOperatingHours(operatingHoursDtos);
            return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원영업시간 등록 성공", null), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new CommonResDto(HttpStatus.BAD_REQUEST, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    // 병원 영업시간 리스트 조회
    @GetMapping("/detail/{hospitalId}")
    public ResponseEntity<Object> getOperatingHours(@PathVariable Long hospitalId){
        try{
            List<HospitalOperatingHoursResDto> operatingHoursList = hospitalOperatingHoursService.getOperatingHoursByHospitalId(hospitalId);
            return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원영업시간 조회 성공", operatingHoursList),HttpStatus.OK);
        }catch (BaseException e){
            return new ResponseEntity<>(new CommonResDto(HttpStatus.BAD_REQUEST, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }

    }

    // 병원 영업시간 리스트 조회
    @GetMapping("/admin/detail")
    public ResponseEntity<Object> adminOperatingHours(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String adminEmail = userDetails.getUsername();
        List<HospitalOperatingHoursResDto> operatingHoursList = hospitalOperatingHoursService.adminOperatingHoursByHospitalId(adminEmail);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원영업시간 조회 성공", operatingHoursList),HttpStatus.OK);
    }

    // 병원 휴게시간 조회
    @GetMapping("/getBreakTime/{hospitalId}")
    public List<HospitalOperatingHoursResDto> getBreakTimes(@PathVariable Long hospitalId){
        List<HospitalOperatingHoursResDto> breakTimes = hospitalOperatingHoursService.getOperatingHoursByHospitalId(hospitalId);
        return breakTimes;
    }

    // 병원 특정 영업시간 수정 (병원 admin만 가능)
    @PreAuthorize("hasRole('HOSPITAL')")
    @PostMapping("/update/{operatingHoursId}")
    public ResponseEntity<Object> updateOperatingHours(@PathVariable Long operatingHoursId,
                                                       @RequestBody HospitalOperatingHoursReqDto dto) {
        try{
            hospitalOperatingHoursService.updateOperatingHours(operatingHoursId, dto);
            return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원영업시간 수정 성공", operatingHoursId), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new CommonResDto(HttpStatus.BAD_REQUEST, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        } catch (BaseException e) {
            return new ResponseEntity<>(new CommonResDto(HttpStatus.CONFLICT, e.getMessage(), null), HttpStatus.CONFLICT);
        }
    }

    // 병원 특정 영업시간 삭제 (병원 admin만 가능)
    @PreAuthorize("hasRole('HOSPITAL')")
    @DeleteMapping("/delete/{operatingHoursId}")
    public ResponseEntity<Object> deleteOperatingHours(@PathVariable Long operatingHoursId){
        try{
            hospitalOperatingHoursService.deleteOperatingHours(operatingHoursId);
            return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원영업시간 삭제 성공", operatingHoursId), HttpStatus.OK);
        }catch (BaseException e){
            return new ResponseEntity<>(new CommonResDto(HttpStatus.BAD_REQUEST, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

}
