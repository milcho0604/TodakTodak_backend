package com.padaks.todaktodak.member.service;

import com.padaks.todaktodak.config.MailUtil;
import com.padaks.todaktodak.member.dto.*;
import com.padaks.todaktodak.member.repository.EmailVerificationRepositoryRedis;
import com.padaks.todaktodak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberAuthService {
    private final MemberRepository memberRepository;
    private final EmailVerificationRepositoryRedis emailVerificationRepositoryRedis;
    private final MailUtil mailUtil; // MailUtil 주입

    /**
     * 이메일 인증번호 발송
     */
    public EmailVerificationDtoResponse sendSignUpVerificationEmail(SignUpVerificationSendEmailDtoRequest request) {
        checkDuplicateEmail(request.getEmail()); // 이메일 중복 검사

        String verificationToken = generateVerificationToken();
        String verificationNumber = generateVerificationNumber();

        EmailVerificationDto emailVerificationDto = new EmailVerificationDto(
                request.getEmail(),
                verificationToken,
                verificationNumber
        );
        emailVerificationRepositoryRedis.save("SignUp", emailVerificationDto, 30); // 30분 타임아웃

        mailUtil.send(new SenderDto(
                Collections.singletonList(request.getEmail()), // 수정된 SenderDto에 맞춰 변경
                "회원가입 인증번호 안내",
                String.format("회원가입을 위해서 아래 인증코드를 입력해주세요.<br>인증번호는 <b>%s</b> 입니다.", verificationNumber)
        ));

        return emailVerificationDto.toResponse();
    }

    private void checkDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
    }

    private String generateVerificationToken() {
        return RandomStringUtils.randomAlphanumeric(32);
    }

    private String generateVerificationNumber() {
        return RandomStringUtils.randomNumeric(6);
    }

    /**
     * 이메일 인증번호 확인
     */
    public EmailVerificationDto checkSignUpVerificationEmail(String token, String verificationNumber) {
        EmailVerificationDto emailVerificationDto = emailVerificationRepositoryRedis.findByVerificationToken("SignUp", token);

        if (emailVerificationDto == null) {
            throw new IllegalArgumentException("이메일 인증 시간이 초과되었습니다. 재시도해주세요.");
        }

        if (emailVerificationDto.isDone()) {
            throw new IllegalArgumentException("이미 이메일 인증이 완료된 상태입니다.");
        }

        if (emailVerificationDto.getAttemptCount() > 10) {
            throw new IllegalArgumentException("너무 많은 시도를 하였습니다. 처음부터 재시도해주세요.");
        }

        if (!emailVerificationDto.getVerificationNumber().equals(verificationNumber)) {
            emailVerificationDto.setAttemptCount(emailVerificationDto.getAttemptCount() + 1);
            emailVerificationRepositoryRedis.save("SignUp", emailVerificationDto, 30);
            throw new IllegalArgumentException("인증번호가 올바르지 않습니다.");
        }

        emailVerificationDto.setDone(true);
        emailVerificationRepositoryRedis.save("SignUp", emailVerificationDto, 15); // 15분 타임아웃
        return emailVerificationDto;
    }

    /**
     * 회원가입
     */
    public void signUp(MemberSaveReqDto request, String verificationToken) {
        EmailVerificationDto emailVerificationDto = validateEmailVerification("SignUp", verificationToken);
        // TODO: 회원가입 로직 작성

        // 이메일 인증 완료 후 회원가입 처리
        completeEmailVerification("SignUp", verificationToken);
    }

    private EmailVerificationDto validateEmailVerification(String name, String token) {
        EmailVerificationDto emailVerificationDto = emailVerificationRepositoryRedis.findByVerificationToken(name, token);

        if (emailVerificationDto == null) {
            throw new IllegalArgumentException("시간이 초과되었습니다. 재시도해주세요.");
        }

        if (!emailVerificationDto.isDone()) {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }

        return emailVerificationDto;
    }

    private void completeEmailVerification(String name, String token) {
        emailVerificationRepositoryRedis.deleteByVerificationToken(name, token);
    }
}
