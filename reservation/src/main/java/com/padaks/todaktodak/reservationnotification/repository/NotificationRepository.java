package com.padaks.todaktodak.reservationnotification.repository;

import com.padaks.todaktodak.reservationnotification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
