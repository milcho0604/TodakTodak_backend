package com.padaks.todaktodak.child.repository;

import com.padaks.todaktodak.child.domain.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {
    Optional<Child> findBySsn(String ssn);
}
