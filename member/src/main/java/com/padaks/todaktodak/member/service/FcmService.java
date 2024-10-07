package com.padaks.todaktodak.member.service;

import com.google.firebase.messaging.*;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.dto.FcmTokenSaveRequest;
import com.padaks.todaktodak.member.repository.MemberRepository;
import com.padaks.todaktodak.notification.domain.Notification;
import com.padaks.todaktodak.notification.domain.Type;
import com.padaks.todaktodak.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class FcmService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
//    public void saveFcmToken(Long memberId, FcmTokenSaveRequest dto){
    public void saveFcmToken(String memberEmail, FcmTokenSaveRequest dto){
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
        member.updateFcmToken(dto.getFcmToken());
        System.out.println(member);
    }

    public void sendMessage(String memberEmail, String title, String body, Type type, Long id){
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));


        String token = member.getFcmToken();

        if (token == null || token.isEmpty()) {
            throw new EntityNotFoundException("FCM token not found for memberId: " + memberEmail);
        }


        Notification notification = Notification.builder()
                .member(member)
                .content(title +"\n"+ body)  //알림 내용 저장
                .isRead(false)      //notification 생성될때 = false -> 사용자가 알림 누르는 순간 true로 바껴야함
                .type(type)
                .refId(id)        //등록된 post의 Id
                .build();

        //db에 Notification 저장
        notificationRepository.save(notification);
        String urlType = null;
        if (type.equals(Type.POST) || type.equals(Type.COMMENT)){urlType = "post";} //url 이동을 위해 변환
        if (type.equals(Type.RESERVATION_NOTIFICATION) || type.equals(Type.RESERVATION_WAITING)) {urlType = "reservation";}
        if (type.equals(Type.PAYMENT)){ urlType = String.valueOf(type);}
        String url = "http://localhost:8081/" + urlType + id ;
        Message message = Message.builder()
                .setWebpushConfig(WebpushConfig.builder()
                        .setNotification(WebpushNotification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                        .build())
                .putData("url", url) //이동할 url 추가
                .setToken(token)
                .build();

        try {
            // 비동기 처리 결과 기다리기
            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            System.out.println("Successfully sent message: " + response);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 스레드 상태 복원
            throw new RuntimeException("Thread was interrupted during FCM message sending: " + e.getMessage(), e);
        } catch (ExecutionException e) {
            throw new RuntimeException("FCM 메시지 전송 중 오류 발생: " + e.getCause().getMessage(), e.getCause());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during FCM message sending: " + e.getMessage(), e);
        }

    }
}
