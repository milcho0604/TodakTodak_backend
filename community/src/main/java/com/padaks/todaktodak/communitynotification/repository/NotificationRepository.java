package com.padaks.todaktodak.communitynotification.repository;

import com.padaks.todaktodak.reservationnotification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
