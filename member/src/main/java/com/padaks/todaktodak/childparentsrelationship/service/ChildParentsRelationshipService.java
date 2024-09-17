package com.padaks.todaktodak.childparentsrelationship.service;

import com.padaks.todaktodak.child.domain.Child;
import com.padaks.todaktodak.childparentsrelationship.domain.ChildParentsRelationship;
import com.padaks.todaktodak.childparentsrelationship.repository.ChildParentsRelationshipRepository;
import com.padaks.todaktodak.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChildParentsRelationshipService {
    private final ChildParentsRelationshipRepository childParentsRelationshipRepository;
    public void createRelationship(Child child, Member member) {
        ChildParentsRelationship childParentsRelationship = ChildParentsRelationship.builder()
                .child(child)
                .member(member)
                .build();
        childParentsRelationshipRepository.save(childParentsRelationship);
    }
}
