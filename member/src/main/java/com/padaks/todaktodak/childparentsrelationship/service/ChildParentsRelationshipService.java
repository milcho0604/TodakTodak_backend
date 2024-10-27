package com.padaks.todaktodak.childparentsrelationship.service;

import com.padaks.todaktodak.child.domain.Child;
import com.padaks.todaktodak.childparentsrelationship.domain.ChildParentsRelationship;
import com.padaks.todaktodak.childparentsrelationship.repository.ChildParentsRelationshipRepository;
import com.padaks.todaktodak.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChildParentsRelationshipService {
    private final ChildParentsRelationshipRepository childParentsRelationshipRepository;
    public void createRelationship(Child child, Member member) {

        Optional<ChildParentsRelationship> byChildAndMemberAndDeletedAtIsNull = childParentsRelationshipRepository.findByChildAndMemberAndDeletedAtIsNull(child, member);
        if (byChildAndMemberAndDeletedAtIsNull.isPresent()) {
            throw new IllegalArgumentException("이미 공유된 자녀입니다.");
        }

        ChildParentsRelationship childParentsRelationship = ChildParentsRelationship.builder()
                .child(child)
                .member(member)
                .build();
        childParentsRelationshipRepository.save(childParentsRelationship);
    }

    public List<Member> findParents(Child child) {
        return childParentsRelationshipRepository.findByChild(child)
                .stream()
                .map(ChildParentsRelationship::getMember)
                .collect(Collectors.toList());
    }
}
