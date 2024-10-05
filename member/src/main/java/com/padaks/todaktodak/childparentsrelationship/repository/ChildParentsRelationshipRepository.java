package com.padaks.todaktodak.childparentsrelationship.repository;

import com.padaks.todaktodak.child.domain.Child;
import com.padaks.todaktodak.childparentsrelationship.domain.ChildParentsRelationship;
import com.padaks.todaktodak.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChildParentsRelationshipRepository extends JpaRepository<ChildParentsRelationship, Long> {
    List<ChildParentsRelationship> findByChild(Child child);
    List<ChildParentsRelationship> findByMemberAndDeletedAtIsNull(Member member);
    Optional<ChildParentsRelationship> findByChildAndMemberAndDeletedAtIsNull(Child child, Member member);

}
