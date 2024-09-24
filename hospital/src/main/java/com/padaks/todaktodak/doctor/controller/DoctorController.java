package com.padaks.todaktodak.doctor.controller;

import com.padaks.todaktodak.common.dto.CommonErrorDto;
import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.doctor.domain.Doctor;
import com.padaks.todaktodak.doctor.dto.*;
import com.padaks.todaktodak.doctor.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctor")
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping("/register")
    public ResponseEntity<Object> doctorCreate(@RequestBody DoctorSaveDto dto){
        try {
            Doctor doctor = doctorService.doctorCreate(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.CREATED, "의사등록에 성공하였습니다.", doctor.getId());
            return new ResponseEntity<>(commonResDto, HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> doctorLogin(@RequestBody DoctorLoginDto dto){
        try {
            String token = doctorService.loginDoctor(dto);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"로그인 성공",token);
            return new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (RuntimeException e){
            e.printStackTrace();
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.UNAUTHORIZED, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/list")
    public ResponseEntity<Object> doctorList(Pageable pageable){
        Page<DoctorListDto> doctorListDtos = doctorService.doctorList(pageable);
        CommonResDto commonResDto = new CommonResDto(HttpStatus.OK, "의사 목록을 조회합니다.", doctorListDtos);
        return new ResponseEntity<>(commonResDto, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDoctorInfo(@RequestBody DoctorUpdateReqDto dto,
                                              @RequestPart(value = "image", required = false)MultipartFile imageSsr){
        try {
            doctorService.updateDoctor(dto, imageSsr);
            CommonResDto commonResDto = new CommonResDto(HttpStatus.OK,"의사 정보가 수정되었습니다." , dto.getId());
            return  new ResponseEntity<>(commonResDto, HttpStatus.OK);
        }catch (EntityNotFoundException e){
            e.printStackTrace();;
            CommonErrorDto commonErrorDto = new CommonErrorDto(HttpStatus.NOT_FOUND, e.getMessage());
            return new ResponseEntity<>(commonErrorDto, HttpStatus.NOT_FOUND);
        }
    }

    // 회원 탈퇴
    @PostMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Map<String, String> request) {
        try {
            String confirmation = request.get("confirmation");
            if (!"토닥 회원 탈퇴에 동의합니다".equals(confirmation)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new CommonResDto(HttpStatus.BAD_REQUEST, "회원 탈퇴 문구가 올바르지 않습니다.", null));
            }
            doctorService.deleteAccount(userDetails.getUsername());
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "회원 탈퇴에 성공하였습니다.", userDetails.getUsername()));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "회원 탈퇴에 실패하였습니다", null));
        }
    }

    // java 라이브러리 메일 서비스 : 인증 코드 전송
    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody EmailVerificationDto verificationDto) {
        doctorService.sendVerificationEmail(verificationDto.getMemberEmail());
        return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "인증 코드 전송에 성공하였습니다.", null));
    }
    // java 라이브러리 메일 서비스 : 인증 코드 확인
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailVerificationDto verificationDto) {
        try {
            boolean isVerified = doctorService.verifyEmail(verificationDto.getMemberEmail(), verificationDto.getCode());
            if (isVerified) {
                return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "이메일 인증에 성공하였습니다.", isVerified));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new CommonResDto(HttpStatus.BAD_REQUEST, "이메일 인증에 실패했습니다.", null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "이메일 인증에 실패했습니다: " + e.getMessage(), null));
        }
    }
}

