package com.padaks.todaktodak.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.padaks.todaktodak.member.service.FcmService;
import com.padaks.todaktodak.notification.domain.Type;
import com.padaks.todaktodak.notification.dto.PaymentCancelDto;
import com.padaks.todaktodak.notification.dto.PaymentCancelFailDto;
import com.padaks.todaktodak.notification.dto.PaymentFailDto;
import com.padaks.todaktodak.notification.dto.PaymentSuccessDto;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentEventListener {

    private final ObjectMapper objectMapper;
    private final FcmService fcmService;

    @KafkaListener(topics = "payment-success", groupId = "payment-group", containerFactory = "payKafkaListenerContainerFactory")
    public void listenPaymentSuccess(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
            // 이스케이프된 문자열을 한번 더 파싱
            String parsedMessage = message.replaceAll("\\\\", "");  // 백슬래시를 제거
            parsedMessage = parsedMessage.substring(1, parsedMessage.length() - 1);  // 양쪽의 큰 따옴표 제거

            // 파싱된 메시지를 DTO로 변환
            PaymentSuccessDto paymentSuccessDto = objectMapper.readValue(parsedMessage, PaymentSuccessDto.class);
            System.out.println("Received Payment Success DTO: " + paymentSuccessDto);

            // 회원 알림 처리
            fcmService.sendMessage(paymentSuccessDto.getMemberEmail(), paymentSuccessDto.getName() + " 결제 알림",
                    paymentSuccessDto.getFee() + "원 결제가 완료되었습니다.", Type.PAYMENT, null);

            // 관리자 알림 처리
            fcmService.sendMessage(paymentSuccessDto.getAdminEmail(), paymentSuccessDto.getName() + " 결제 알림",
                    paymentSuccessDto.getMemberEmail() + "님 " + paymentSuccessDto.getFee() + "원 결제 완료", Type.PAYMENT, null);

            // 메시지 처리 후 수동 오프셋 커밋
            acknowledgment.acknowledge();
        } catch (Exception e) {
            System.err.println("Error processing payment success message: " + e.getMessage());
        }
    }


    @KafkaListener(topics = "payment-fail", groupId = "payment-group", containerFactory = "payKafkaListenerContainerFactory")
    public void listenPaymentFail(String message, Acknowledgment acknowledgment) throws JsonProcessingException {

        PaymentFailDto paymentFailDto = objectMapper.readValue(message, PaymentFailDto.class);
        System.out.println("Received Payment Fail DTO: " + paymentFailDto);

        // 결제 실패 후 알림을 보낼 로직
        // 회원 알림
        fcmService.sendMessage(paymentFailDto.getMemberEmail(), "결제에 실패하였습니다.",
                "impUid: " + paymentFailDto.getImpUid() + "의 결제건이 결제에 실패했습니다. 관리자에게 문의해주세요.", Type.PAYMENT, null);

        // 관리자 알림
        fcmService.sendMessage(paymentFailDto.getAdminEmail(), "결제에 실패하였습니다.",
                paymentFailDto.getMemberEmail() + "님의" + "impUid: " + paymentFailDto.getImpUid() + "의 결제건이 처리에 실패하였습니다.", Type.PAYMENT, null);

        // 오프셋 변경
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "payment-cancel", groupId = "payment-group", containerFactory = "payKafkaListenerContainerFactory")
    public void listenPaymentCancel(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        System.out.println("Received Payment Cancel message: " + message);

        PaymentCancelDto paymentCancelDto = objectMapper.readValue(message, PaymentCancelDto.class);
        System.out.println("Received Payment Cancel DTO: " + paymentCancelDto);
        // 결제 취소 후 알림을 보낼 로직
        // 회원 알림
        fcmService.sendMessage(paymentCancelDto.getMemberEmail(), paymentCancelDto.getName() + "의 결제 취소가 완료되었습니다.",
                paymentCancelDto.getFee() + "원이 결제 취소되었습니다.", Type.PAYMENT, null);

        // 토닥 관리자 알림
        fcmService.sendMessage(paymentCancelDto.getAdminEmail(), paymentCancelDto.getName() + "의 결제 취소가 완료되었습니다.",
                paymentCancelDto.getMemberEmail() + "님의" + paymentCancelDto.getFee() + "원이 결제 취소되었습니다.", Type.PAYMENT, null);


        // 오프셋 변경
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "payment-cancel-fail", groupId = "payment-group", containerFactory = "payKafkaListenerContainerFactory")
    public void listenPaymentCancelFail(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        System.out.println("Received Payment Cancel message: " + message);

        PaymentCancelFailDto paymentCancelFailDto = objectMapper.readValue(message, PaymentCancelFailDto.class);
        System.out.println("Received Payment Cancel Fail DTO: " + paymentCancelFailDto);
        // 결제 취소 후 알림을 보낼 로직

        // 회원 알림
        fcmService.sendMessage(paymentCancelFailDto.getMemberEmail(), "impUid: " + paymentCancelFailDto.getImpUid() + "의 결제 취소에 실패했습니다..",
                paymentCancelFailDto.getFee() + "원이 결제 취소에 실패했습니다. 관리자에게 문의해주세요.", Type.PAYMENT, null);

        // 토닥 관리자 알림
        fcmService.sendMessage(paymentCancelFailDto.getAdminEmail(), "impUid: " + paymentCancelFailDto.getImpUid() + "의 결제 취소에 실패했습니다..",
                paymentCancelFailDto.getMemberEmail()+"님이 요청하신 " + paymentCancelFailDto.getFee() + "원이 결제 취소에 실패했습니다", Type.PAYMENT, null);

        // 오프셋 변경
        acknowledgment.acknowledge();
    }
}
