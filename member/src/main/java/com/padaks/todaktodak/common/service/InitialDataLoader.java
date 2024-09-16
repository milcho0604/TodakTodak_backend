package com.padaks.todaktodak.common.service;

import com.padaks.todaktodak.member.domain.Role;
import com.padaks.todaktodak.member.dto.AdminSaveDto;
import com.padaks.todaktodak.member.repository.MemberRepository;
import com.padaks.todaktodak.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements CommandLineRunner {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception{
        if(memberRepository.findByMemberEmail("todak@test.com").isEmpty()){
            memberService.adminCreate(AdminSaveDto.builder()
                            .memberEmail("todak@test.com")
                            .name("Admin")
                            .password(passwordEncoder.encode("12341234"))
                            .phoneNumber("010-1111-2222")
                            .role(Role.TodakAdmin)
                    .build());
        }
    }
}
