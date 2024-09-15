package com.padaks.todaktodak.member.service;

import com.padaks.todaktodak.config.JwtTokenprovider;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.dto.MemberUpdateReqDto;
import com.padaks.todaktodak.member.dto.MemberLoginDto;
import com.padaks.todaktodak.member.dto.MemberSaveReqDto;
import com.padaks.todaktodak.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Random;

@Service
@Slf4j
@Transactional
public class MemberService {
    private MemberRepository memberRepository;
    private JwtTokenprovider jwtTokenprovider;
    private PasswordEncoder passwordEncoder;
    private JavaEmailService emailService;
    private RedisService redisService;

    @Autowired
    public MemberService(MemberRepository memberRepository, JwtTokenprovider jwtTokenprovider, PasswordEncoder passwordEncoder, JavaEmailService emailService, RedisService redisService) {
        this.memberRepository = memberRepository;
        this.jwtTokenprovider = jwtTokenprovider;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.redisService = redisService;
    }

    // 간편하게 멤버 객체를 찾기 위한 findByEmail
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다.11"));
    }

    // 회원가입 및 검증
    public void create(MemberSaveReqDto saveReqDto) {
        validateRegistration(saveReqDto);
        Member member = saveReqDto.toEntity(passwordEncoder.encode(saveReqDto.getPassword()));
        memberRepository.save(member);
    }

    // 로그인
    public String login(MemberLoginDto loginDto) {
        Member member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("잘못된 이메일/비밀번호 입니다."));

        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new RuntimeException("잘못된 이메일/비밀번호 입니다.");
        }

        if (member.getDeletedTimeAt() != null){
            throw new IllegalStateException("탈퇴한 회원입니다.");
        }

        return jwtTokenprovider.createToken(member.getEmail(), member.getRole().name(), member.getId());
    }

    // 회원가입 검증 로직
    private void validateRegistration(MemberSaveReqDto saveReqDto) {
        if (saveReqDto.getPassword().length() <= 7) {
            throw new RuntimeException("비밀번호는 8자 이상이어야 합니다.");
        }

        if (memberRepository.existsByEmail(saveReqDto.getEmail())) {
            throw new RuntimeException("이미 사용중인 이메일 입니다.");
        }
    }
    // 비밀번호 확인 및 검증 로렢
    private void validatePassword(String newPassword, String confirmPassword, String currentPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("동일하지 않은 비밀번호 입니다.");
        }

        if (newPassword.length() <= 7) {
            throw new RuntimeException("비밀번호는 8자 이상이어야 합니다.");
        }

        if (passwordEncoder.matches(newPassword, currentPassword)) {
            throw new RuntimeException("이전과 동일한 비밀번호로 설정할 수 없습니다.");
        }
    }

    // 회원 정보 수정
    public void updateMember(Member member, MemberUpdateReqDto editReqDto){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member savedMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다"));
        if (editReqDto.getPassword() != null && !editReqDto.getPassword().isEmpty()) {
            validatePassword(editReqDto.getPassword(), editReqDto.getConfirmPassword(), member.getPassword());
            savedMember.setPassword(passwordEncoder.encode(editReqDto.getPassword()));
        }
        if (editReqDto.getAddress() != null) {
            savedMember.setAddress(editReqDto.getAddress());
        }
        savedMember.setName(editReqDto.getName());
        memberRepository.save(savedMember);
    }

    // 회원 탈퇴
    public void deleteAccount(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자 정보입니다."+ email));
        member.deleteAccount();
        memberRepository.save(member);
    }

    // java 라이브러리 메일 서비스
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(9999 - 1000 + 1) + 1000;
        return String.valueOf(code);
    }
    // java 라이브러리 메일 서비스
    public void sendVerificationEmail(String email) {
        String code = generateVerificationCode();
        redisService.saveVerificationCode(email, code);
        emailService.sendSimpleMessage(email, "이메일 인증 코드", "인증 코드: " + code);
    }
    // java 라이브러리 메일 서비스
    public boolean verifyEmail(String email, String code) {
        if (redisService.verifyCode(email, code)) {
            return true;
        } else {
            throw new RuntimeException("인증 코드가 유효하지 않습니다.");
        }
    }



}
