package com.padaks.todaktodak.member.controller;

import com.padaks.todaktodak.common.dto.CommonErrorDto;
import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.dto.*;
import com.padaks.todaktodak.member.service.MemberAuthService;
import com.padaks.todaktodak.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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

    // 회원가입
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

    // 로그인
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

    // 이메일 인증 코드 발송
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

    // 이메일 인증 확인 로직
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

    // 회원 정보 수정
    @PostMapping("edit-info")
    public ResponseEntity<?> editMemberInfo(@RequestBody MemberUpdateReqDto updateReqDto) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Member member = memberService.findByEmail(email);
            memberService.updateMember(member, updateReqDto);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "회원 정보를 수정하였습니다.", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "회원 정보 수정에 실패했습니다. -> " + e.getMessage(), null));
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
            memberService.deleteAccount(userDetails.getUsername());
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
        memberService.sendVerificationEmail(verificationDto.getEmail());
        return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "인증 코드 전송에 성공하였습니다.", null));
    }
    // java 라이브러리 메일 서비스 : 인증 코드 확인
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody JavaEmailVerificationDto verificationDto) {
        try {
            boolean isVerified = memberService.verifyEmail(verificationDto.getEmail(), verificationDto.getCode());
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
