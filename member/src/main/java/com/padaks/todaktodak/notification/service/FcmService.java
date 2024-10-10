package com.padaks.todaktodak.notification.service;

import com.google.firebase.messaging.*;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.dto.FcmTokenSaveRequest;
import com.padaks.todaktodak.member.repository.MemberRepository;
import com.padaks.todaktodak.notification.domain.FcmNotification;
import com.padaks.todaktodak.notification.domain.Type;
import com.padaks.todaktodak.notification.dto.NotificationResDto;
import com.padaks.todaktodak.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class FcmService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void saveFcmToken(String memberEmail, FcmTokenSaveRequest dto) {
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
        member.updateFcmToken(dto.getFcmToken());
        System.out.println(member);
    }

    public void sendMessage(String memberEmail, String title, String body, Type type, Long id) {
        System.out.println(memberEmail);
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));


        String token = member.getFcmToken();

        if (token == null || token.isEmpty()) {
            throw new EntityNotFoundException("FCM token not found for memberId: " + memberEmail);
        }


        FcmNotification fcmNotification = FcmNotification.builder()
                .member(member)
                .content(title + "\n" + body)  //알림 내용 저장
                .isRead(false)      //fcmNotification 생성될때 = false -> 사용자가 알림 누르는 순간 true로 바껴야함
                .type(type)
                .refId(id)        //등록된 post의 Id
                .recipient(memberEmail)
                .build();

        //db에 FcmNotification 저장
        notificationRepository.save(fcmNotification);

        String urlType = null;
        if (type.equals(Type.POST) || type.equals(Type.COMMENT)) {
            urlType = "post";
        } else if (type.equals(Type.RESERVATION_NOTIFICATION) || type.equals(Type.RESERVATION_WAITING)) {
            urlType = "reservation";
        } else if (type.equals(Type.PAYMENT)) {
            urlType = String.valueOf(type);
        } else {
            urlType = "/"; // 추가 로직 설정 필요
        }

        String url = "http://localhost:8081/" + urlType + "/" + id;
        Message message = Message.builder()
                .setWebpushConfig(WebpushConfig.builder()
                        .setNotification(WebpushNotification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                        .build())
                .putData("url", url) //이동할 url 추가
                .putData("notificationId", String.valueOf(fcmNotification.getId())) //이동할 url 추가
                .setToken(token)
                .build();

        try {
            // 비동기 처리 결과 기다리기
            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            System.out.println("Successfully send message: " + response);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 스레드 상태 복원
            e.printStackTrace();
            throw new RuntimeException("Thread was interrupted during FCM message sending: " + e.getMessage(), e);
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException("FCM 메시지 전송 중 오류 발생: " + e.getCause().getMessage(), e.getCause());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during FCM message sending: " + e.getMessage(), e);
        }
    }


    public Page<NotificationResDto> myNotis(Pageable pageable) {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다."));
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        Page<FcmNotification> notifications = notificationRepository.findByMemberAndCreatedAtAfter(member, sevenDaysAgo, pageable);
        return notifications.map(fcmNotification -> new NotificationResDto().fromEntity(fcmNotification));
    }

    public FcmNotification read(Long id) {
        FcmNotification fcmNotification = notificationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("알림이 존재하지 않습니다."));
        fcmNotification.read();
        notificationRepository.save(fcmNotification);
        System.out.println("read 검증:" + fcmNotification.isRead());
        return fcmNotification;
    }
}
