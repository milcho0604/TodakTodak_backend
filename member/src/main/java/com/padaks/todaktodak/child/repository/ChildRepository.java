package com.padaks.todaktodak.child.repository;

import com.padaks.todaktodak.child.domain.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {
}
