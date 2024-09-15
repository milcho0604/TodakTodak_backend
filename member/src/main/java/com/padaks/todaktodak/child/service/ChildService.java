package com.padaks.todaktodak.child.service;

import com.padaks.todaktodak.child.domain.Child;
import com.padaks.todaktodak.child.dto.ChildResDto;
import com.padaks.todaktodak.child.dto.ChildUpdateReqDto;
import com.padaks.todaktodak.child.repository.ChildRepository;
import com.padaks.todaktodak.childparentsrelationship.domain.ChildParentsRelationship;
import com.padaks.todaktodak.childparentsrelationship.repository.ChildParentsRelationshipRepository;
import com.padaks.todaktodak.childparentsrelationship.service.ChildParentsRelationshipService;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChildService {
    private final ChildRepository childRepository;
    private final MemberRepository memberRepository;
    private final ChildParentsRelationshipService childParentsRelationshipService;
    private final ChildParentsRelationshipRepository childParentsRelationshipRepository;

    public void createChild(String name, String ssn) {
        Child child = Child.builder()
                .name(name)
                .ssn(ssn)
                .build();
        childRepository.save(child);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다"));
        childParentsRelationshipService.createRelationship(child, member);
    }

    public void updateChild(ChildUpdateReqDto dto) {
        Child child = childRepository.findById(dto.getChildId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 자녀입니다"));
        child.updateName(dto.getName());
    }

    public List<ChildResDto> myChild() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByMemberEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다"));
        List<ChildParentsRelationship> childParentsRelationships = childParentsRelationshipRepository.findByMember(member);
        List<ChildResDto> childList = new ArrayList<>();
        for (ChildParentsRelationship childParentsRelationship : childParentsRelationships) {
            Child child = childParentsRelationship.getChild();
            childList.add(new ChildResDto().fromEntity(child));
        }
        return childList;
    }
}
