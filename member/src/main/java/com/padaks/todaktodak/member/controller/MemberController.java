package com.padaks.todaktodak.member.controller;

import com.padaks.todaktodak.common.dto.CommonErrorDto;
import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.member.dto.*;
import com.padaks.todaktodak.member.service.MemberAuthService;
import com.padaks.todaktodak.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberAuthService memberAuthService;

    @Autowired
    public MemberController(MemberService memberService, MemberAuthService memberAuthService) {
        this.memberService = memberService;
        this.memberAuthService = memberAuthService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> register(@RequestBody MemberSaveReqDto saveReqDto) {
        try {
            memberService.create(saveReqDto);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "회원가입 성공", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "회원가입에 실패했습니다: " + e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginDto loginDto) {
        try {
            String token = memberService.login(loginDto);
            System.out.println("Generated JWT Token: " + token);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "로그인 성공", token));
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (e.getMessage().contains("비활성화 상태")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new CommonErrorDto(HttpStatus.UNAUTHORIZED, e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonErrorDto(HttpStatus.UNAUTHORIZED, "잘못된 이메일/비밀번호입니다."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonErrorDto(HttpStatus.UNAUTHORIZED, "로그인 중 오류가 발생했습니다."));
        }
    }

    @PostMapping("/verification/email/send")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody SignUpVerificationSendEmailDtoRequest requestDto) {
        try {
            EmailVerificationDtoResponse response = memberAuthService.sendSignUpVerificationEmail(requestDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonErrorDto(HttpStatus.BAD_REQUEST, "이메일 인증 번호 발송에 실패했습니다: " + e.getMessage()));
        }
    }

    @PostMapping("/verification/email/check")
    public ResponseEntity<?> checkVerificationEmail(@RequestBody EmailVerificationDto requestDto) {
        try {
            EmailVerificationDto emailVerificationDto = memberAuthService.checkSignUpVerificationEmail(
                    requestDto.getVerificationToken(), requestDto.getVerificationNumber());

            // 인증 성공 응답
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "이메일 인증이 성공적으로 완료되었습니다.", emailVerificationDto));
        } catch (IllegalArgumentException e) {
            // 인증 실패 응답
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonErrorDto(HttpStatus.BAD_REQUEST, "이메일 인증에 실패했습니다: " + e.getMessage()));
        } catch (Exception e) {
            // 기타 오류 응답
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 인증 처리 중 오류가 발생했습니다."));
        }
    }
}
