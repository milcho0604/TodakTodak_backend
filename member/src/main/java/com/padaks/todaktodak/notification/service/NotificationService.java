package com.padaks.todaktodak.notification.service;

import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.repository.MemberRepository;
import com.padaks.todaktodak.notification.domain.Notification;
import com.padaks.todaktodak.notification.dto.NotificationReqDto;
import com.padaks.todaktodak.notification.dto.NotificationResDto;
import com.padaks.todaktodak.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public NotificationResDto notificationCreate(NotificationReqDto dto) {
        log.info(dto.getMemberEmail());
        Member member = memberRepository.findByMemberEmail(dto.getMemberEmail())
                .orElseThrow(()-> new EntityNotFoundException("회원이 존재하지 않습니다."));
        Notification notification = dto.toEntity(member);
        Notification save = notificationRepository.save(notification);
        return new NotificationResDto().fromEntity(save);

    }

    public Page<NotificationResDto> mynotis(Pageable pageable) {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(()-> new EntityNotFoundException("회원이 존재하지 않습니다."));
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        Page<Notification> notifications = notificationRepository.findByMemberAndCreatedAtAfter(member, sevenDaysAgo, pageable);
        return notifications.map(notification -> new NotificationResDto().fromEntity(notification));
    }

    public NotificationResDto read(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("알림이 존재하지 않습니다."));
        notification.read();
        return null;
    }
}
