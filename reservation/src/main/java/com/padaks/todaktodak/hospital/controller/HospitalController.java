package com.padaks.todaktodak.hospital.controller;

import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.common.dto.MemberFeignDto;
import com.padaks.todaktodak.hospital.domain.Hospital;
import com.padaks.todaktodak.hospital.dto.*;
import com.padaks.todaktodak.hospital.repository.HospitalRepository;
import com.padaks.todaktodak.hospital.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hospital")
public class HospitalController {
    private final HospitalService hospitalService;
    private final HospitalRepository hospitalRepository;

    // 병원생성 (병원 admin만 가능) 이후 주석해제 예정
//    @PreAuthorize("hasRole('ROLE_HOSPTIALADMIN')")
    @PostMapping("/register")
    public ResponseEntity<Object> registerHospital(@ModelAttribute HospitalRegisterReqDto hospitalRegisterReqDto){
        Hospital hospital = hospitalService.registerHospital(hospitalRegisterReqDto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED, "병원등록성공", hospital.getId()), HttpStatus.CREATED);
    }

    // 병원 admin + 병원 등록 (미승인 상태)
//    @PreAuthorize("hasRole('ROLE_HOSPTIALADMIN')")
    @PostMapping("/hospital-admin/register")
    public ResponseEntity<Object> registerHospitalAndAdmin(@RequestBody HospitalAndAdminRegisterReqDto dto){
        HospitalAndAdminRegisterResDto hospitalAndAdminRegisterResDto
                = hospitalService.registerHospitalAndAdmin(dto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED, "병원, 병원 admin 등록 성공 (미승인)", hospitalAndAdminRegisterResDto), HttpStatus.CREATED);
    }
    // 병원 admin + 병원 승인처리
    // 병원 : isAccept = true, 병원 admin : deletedAt = null
    @PutMapping("/accept/{id}")
    public ResponseEntity<Object> acceptHospital(@PathVariable Long id){
        hospitalService.acceptHospital(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원, 병원 admin 가입 승인처리 성공", null),HttpStatus.OK);
    }

    // 병원 detail 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<Object> getHospitalDetail(@PathVariable Long id,
                                                    @RequestParam BigDecimal latitude,
                                                    @RequestParam BigDecimal longitude) {
        HospitalDetailResDto hospitalDetail = hospitalService.getHospitalDetail(id, latitude, longitude);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원정보 조회성공", hospitalDetail), HttpStatus.OK);
    }

    // 병원정보 수정 (병원 admin, 개발자admin)
    @PostMapping("/update")
    public ResponseEntity<Object> updateHospital(@ModelAttribute HospitalUpdateReqDto hospitalUpdateReqDto){
        HospitalUpdateResDto hospitalUpdateResDto = hospitalService.updateHospital(hospitalUpdateReqDto);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원정보 수정성공", hospitalUpdateResDto), HttpStatus.OK);
    }

    // 병원정보 수정 (병원 admin, 개발자admin)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteHospital(@PathVariable Long id){
        hospitalService.deleteHospital(id);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원 삭제성공", id), HttpStatus.OK);
    }

    // 병원리스트 조회
    @GetMapping("/list")
    public ResponseEntity<Object> getList(@RequestParam String dong,
                                          @RequestParam BigDecimal latitude,
                                          @RequestParam BigDecimal longitude){
        List<HospitalListResDto> hospitalList = hospitalService.getHospitalList(dong, latitude, longitude);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원 리스트 조회성공", hospitalList), HttpStatus.OK);
    }

    // 병원 정보를 멤버로 전송
    @GetMapping("/get/hospital")
    public HospitalFeignDto getHospital(){
        MemberFeignDto memberFeignDto = hospitalService.getMemberInfo();
        Long id = memberFeignDto.getHospitalId();
        Hospital hospital = hospitalRepository.findByIdOrThrow(id);
        return new HospitalFeignDto(hospital.getId(), hospital.getName(), hospital.getPhoneNumber());
    }

    @GetMapping("/get/info/{id}")
    public HospitalInfoDto getHospitalinfo(@RequestParam Long id){
        System.out.println("reservation-service는 왔나요");
        Hospital hospital = hospitalRepository.findByIdOrThrow(id);

        HospitalInfoDto dto = HospitalInfoDto.builder()
                .address(hospital.getAddress())
                .name(hospital.getName())
                .dong(hospital.getDong())
                .profileImg(hospital.getHospitalImageUrl())
                .build();
        return dto;
    }

    // 정렬된 병원리스트 조회
    @GetMapping("/sorted/list")
    public ResponseEntity<Object> getSortedHospitalList(@RequestParam String dong,
                                          @RequestParam BigDecimal latitude,
                                          @RequestParam BigDecimal longitude,
                                          @RequestParam String sort
    ){
        List<HospitalListResDto> hospitalList = hospitalService.getSortedHospitalList(dong, latitude, longitude, sort);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "병원 리스트 조회성공", hospitalList), HttpStatus.OK);
    }

}
