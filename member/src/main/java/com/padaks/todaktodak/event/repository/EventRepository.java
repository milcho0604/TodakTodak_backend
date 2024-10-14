package com.padaks.todaktodak.event.repository;

import com.padaks.todaktodak.event.domain.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByMemberEmailAndDeletedAtIsNull(String memberEmail, Pageable pageable);
}
