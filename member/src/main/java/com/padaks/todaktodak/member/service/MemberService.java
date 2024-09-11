package com.padaks.todaktodak.member.service;

import com.padaks.todaktodak.config.JwtTokenprovider;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.dto.MemberLoginDto;
import com.padaks.todaktodak.member.dto.MemberSaveReqDto;
import com.padaks.todaktodak.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class MemberService {
    private MemberRepository memberRepository;
    private JwtTokenprovider jwtTokenprovider;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, JwtTokenprovider jwtTokenprovider, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.jwtTokenprovider = jwtTokenprovider;
        this.passwordEncoder = passwordEncoder;
    }


    public void create(MemberSaveReqDto saveReqDto) {
//        validateRegistration(saveReqDto);

        Member member = saveReqDto.toEntity(passwordEncoder.encode(saveReqDto.getPassword()));
        memberRepository.save(member);
    }

    public String login(MemberLoginDto loginDto) {
        Member member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("잘못된 이메일/비밀번호 입니다."));

        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new RuntimeException("잘못된 이메일/비밀번호 입니다.");
        }

        return jwtTokenprovider.createToken(member.getEmail(), member.getRole().name(), member.getId());
    }

}
