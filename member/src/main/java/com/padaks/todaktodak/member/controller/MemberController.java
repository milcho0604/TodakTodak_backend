package com.padaks.todaktodak.member.controller;

import com.padaks.todaktodak.common.dto.CommonErrorDto;
import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.config.JwtAuthFilter;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.dto.*;
import com.padaks.todaktodak.member.service.CustomMemberDetailsService;
import com.padaks.todaktodak.member.service.MemberAuthService;
import com.padaks.todaktodak.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MemberAuthService memberAuthService;


    @GetMapping("/get/member")
    public MemberDto getMember() {
        // 현재 인증된 사용자 정보 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            String memberEmail = authentication.getName(); // 인증된 사용자의 이메일 가져오기
//            System.out.println("여기는 이메일을 찾는: " + memberEmail);
//            Member member = memberService.findByMemberEmail(memberEmail);
//            // 이메일로 MemberDto 생성 후 반환
//            return new MemberDto(memberEmail, member.getName(), member.getPhoneNumber()); // 예시 데이터
//        }
//        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        if (memberEmail.equals("anonymousUser")){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(email);
        Member member = memberService.findByMemberEmail(email);
        return new MemberDto(member.getMemberEmail(), member.getName(), member.getPhoneNumber());
//        }
//        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 요청입니다.");
    }

    // 회원가입
    @PostMapping("/create")
    public ResponseEntity<?> register(MemberSaveReqDto saveReqDto,
            @RequestPart(value = "image", required = false) MultipartFile imageSsr) {
        try {
            memberService.create(saveReqDto, imageSsr);
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
    @PostMapping("/edit-info")
    public ResponseEntity<?> editMemberInfo(
            @ModelAttribute MemberUpdateReqDto updateReqDto) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Member member = memberService.findByMemberEmail(email);

            // 회원 정보 업데이트
            memberService.updateMember(member, updateReqDto); // profileImage 추가

            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "회원 정보를 수정하였습니다.", member));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResDto(HttpStatus.NOT_FOUND, "회원 정보가 존재하지 않습니다. -> " + e.getMessage(), null));
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
        memberService.sendVerificationEmail(verificationDto.getMemberEmail());
        return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "인증 코드 전송에 성공하였습니다.", null));
    }
    // java 라이브러리 메일 서비스 : 인증 코드 확인
    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody JavaEmailVerificationDto verificationDto) {
        try {
            boolean isVerified = memberService.verifyEmail(verificationDto.getMemberEmail(), verificationDto.getCode());
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

    // member list
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<Object> userList(Pageable pageable){
        Page<MemberListResDto> memberListResDtos = memberService.memberList(pageable);
        CommonResDto dto = new CommonResDto(HttpStatus.OK,"회원목록을 조회합니다.", memberListResDtos);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
