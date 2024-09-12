package com.padaks.todaktodak.member.service;

import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.dto.AdminSaveDto;
import com.padaks.todaktodak.member.dto.DtoMapper;
import com.padaks.todaktodak.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final DtoMapper dtoMapper;

    @Autowired
    public MemberService(MemberRepository memberRepository, DtoMapper dtoMapper) {
        this.memberRepository = memberRepository;
        this.dtoMapper = dtoMapper;
    }

//    유저 회원가입은 소셜만 되어있기 때문에 TodakAdmin만 따로 initialDataLoader로 선언해 주었음.
    public void adminCreate(AdminSaveDto dto){
        log.info("MemberService[adminCreate] 실행 ");
//        DtoMapper의 toMember 를 사용하여 member에 자동으로 mapping 해준다.
        Member member = dtoMapper.toMember(dto);
        memberRepository.save(member);
    }
}
