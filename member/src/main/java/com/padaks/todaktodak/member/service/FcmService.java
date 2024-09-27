package com.padaks.todaktodak.member.service;

import com.google.firebase.messaging.*;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.dto.FcmTokenSaveRequest;
import com.padaks.todaktodak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class FcmService {

    private final MemberRepository memberRepository;

    @Transactional
    public void saveFcmToken(Long memberId, FcmTokenSaveRequest dto){
        Member member = memberRepository.findByIdOrThrow(memberId);
        member.updateFcmToken(dto.getFcmToken());
    }

    public void sendTestMessage(Long memberId){
        Member member = memberRepository.findByIdOrThrow(memberId);
        String token = member.getFcmToken();

        if (token == null || token.isEmpty()){
            throw new EntityNotFoundException("FCM token not found for memberId: " + memberId);
        }

        Message message = Message.builder()
                .setWebpushConfig(WebpushConfig.builder()
                        .setNotification(WebpushNotification.builder()
                                .setTitle("알림 제목")
                                .setBody("알림 내용")
                                .build())
                        .build())
                .setToken(token)
                .build();

        try {
            //비동기 처리 기다림
            FirebaseMessaging.getInstance().sendAsync(message).get();
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 스레드 상태 복원
            throw new RuntimeException("Thread was interrupted during FCM message sending: " + e.getMessage(), e);
        } catch (ExecutionException e) {
            // FCM 메시지 전송 중 발생한 예외 처리
            throw new RuntimeException("FCM 메시지 전송 중 오류 발생: " + e.getCause().getMessage(), e.getCause());
        } catch (Exception e){
            throw new RuntimeException("Unexpected error during FCM message sending: " + e.getMessage(), e);
        }
    }
}
