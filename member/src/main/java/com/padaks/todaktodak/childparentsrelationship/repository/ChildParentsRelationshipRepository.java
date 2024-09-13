package com.padaks.todaktodak.childparentsrelationship.repository;

import com.padaks.todaktodak.childparentsrelationship.domain.ChildParentsRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildParentsRelationshipRepository extends JpaRepository<ChildParentsRelationship, Long> {
}
