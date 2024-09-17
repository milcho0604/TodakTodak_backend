package com.padaks.todaktodak.childparentsrelationship.repository;

import com.padaks.todaktodak.childparentsrelationship.domain.ChildParentsRelationship;
import com.padaks.todaktodak.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildParentsRelationshipRepository extends JpaRepository<ChildParentsRelationship, Long> {
    List<ChildParentsRelationship> findByMember(Member member);
}
