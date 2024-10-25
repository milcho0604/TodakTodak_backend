package com.padaks.todaktodak.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.padaks.todaktodak.notification.domain.Type;
import com.padaks.todaktodak.notification.dto.PaymentCancelDto;
import com.padaks.todaktodak.notification.dto.PaymentCancelFailDto;
import com.padaks.todaktodak.notification.dto.PaymentFailDto;
import com.padaks.todaktodak.notification.dto.PaymentSuccessDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventListener {

    private final ObjectMapper objectMapper;
    private final FcmService fcmService;

    @KafkaListener(topics = "payment-success", groupId = "payment-group", containerFactory = "payKafkaListenerContainerFactory")
    public void listenPaymentSuccess(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
            String parsedMessage = message.replaceAll("\\\\", "");  // 백슬래시 제거
            parsedMessage = parsedMessage.substring(1, parsedMessage.length() - 1);  // 양쪽의 큰 따옴표 제거

            PaymentSuccessDto paymentSuccessDto = objectMapper.readValue(parsedMessage, PaymentSuccessDto.class);
            System.out.println("Received Payment Success DTO: " + paymentSuccessDto);

            fcmService.sendMessage(paymentSuccessDto.getMemberEmail(), paymentSuccessDto.getName() + " 결제 알림",
                    paymentSuccessDto.getFee() + "원 결제가 완료되었습니다.", Type.PAYMENT, null);

            fcmService.sendMessage(paymentSuccessDto.getAdminEmail(), paymentSuccessDto.getName() + " 결제 알림",
                    paymentSuccessDto.getMemberEmail() + "님 " + paymentSuccessDto.getFee() + "원 결제 완료", Type.PAYMENT, null);

            acknowledgment.acknowledge();
        } catch (Exception e) {
            System.err.println("Error processing payment success message: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "payment-fail", groupId = "payment-group", containerFactory = "payKafkaListenerContainerFactory")
    public void listenPaymentFail(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
            String parsedMessage = message.replaceAll("\\\\", "");  // 백슬래시 제거
            parsedMessage = parsedMessage.substring(1, parsedMessage.length() - 1);  // 양쪽의 큰 따옴표 제거

            PaymentFailDto paymentFailDto = objectMapper.readValue(parsedMessage, PaymentFailDto.class);
            System.out.println("Received Payment Fail DTO: " + paymentFailDto);

            fcmService.sendMessage(paymentFailDto.getMemberEmail(), " 결제에 실패하였습니다.",
                    "impUid: " + paymentFailDto.getImpUid() + "의 결제건이 결제에 실패했습니다. 관리자에게 문의해주세요.", Type.PAYMENT, null);

            fcmService.sendMessage(paymentFailDto.getAdminEmail(), " 결제에 실패하였습니다.",
                    paymentFailDto.getMemberEmail() + "님의" + " impUid: " + paymentFailDto.getImpUid() + "의 결제건이 처리에 실패하였습니다.", Type.PAYMENT, null);

            acknowledgment.acknowledge();
        } catch (Exception e) {
            System.err.println("Error processing payment fail message: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "payment-cancel", groupId = "payment-group", containerFactory = "payKafkaListenerContainerFactory")
    public void listenPaymentCancel(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
            String parsedMessage = message.replaceAll("\\\\", "");  // 백슬래시 제거
            parsedMessage = parsedMessage.substring(1, parsedMessage.length() - 1);  // 양쪽의 큰 따옴표 제거

            PaymentCancelDto paymentCancelDto = objectMapper.readValue(parsedMessage, PaymentCancelDto.class);
            System.out.println("Received Payment Cancel DTO: " + paymentCancelDto);

            fcmService.sendMessage(paymentCancelDto.getMemberEmail(), paymentCancelDto.getName() + "의 결제 취소가 완료되었습니다.",
                    paymentCancelDto.getFee() + "원이 결제 취소되었습니다.", Type.PAYMENT, null);

            fcmService.sendMessage(paymentCancelDto.getAdminEmail(), paymentCancelDto.getName() + "의 결제 취소가 완료되었습니다.",
                    paymentCancelDto.getMemberEmail() + "님의" + paymentCancelDto.getFee() + "원이 결제 취소되었습니다.", Type.PAYMENT, null);

            acknowledgment.acknowledge();
        } catch (Exception e) {
            System.err.println("Error processing payment cancel message: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "payment-cancel-fail", groupId = "payment-group", containerFactory = "payKafkaListenerContainerFactory")
    public void listenPaymentCancelFail(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
            String parsedMessage = message.replaceAll("\\\\", "");  // 백슬래시 제거
            parsedMessage = parsedMessage.substring(1, parsedMessage.length() - 1);  // 양쪽의 큰 따옴표 제거

            PaymentCancelFailDto paymentCancelFailDto = objectMapper.readValue(parsedMessage, PaymentCancelFailDto.class);
            System.out.println("Received Payment Cancel Fail DTO: " + paymentCancelFailDto);

            fcmService.sendMessage(paymentCancelFailDto.getMemberEmail(), "impUid: " + paymentCancelFailDto.getImpUid() + "의 결제 취소에 실패했습니다.",
                    paymentCancelFailDto.getFee() + "원이 결제 취소에 실패했습니다. 관리자에게 문의해주세요.", Type.PAYMENT, null);

            fcmService.sendMessage(paymentCancelFailDto.getAdminEmail(), "impUid: " + paymentCancelFailDto.getImpUid() + "의 결제 취소에 실패했습니다.",
                    paymentCancelFailDto.getMemberEmail() + "님이 요청하신 " + paymentCancelFailDto.getFee() + "원이 결제 취소에 실패했습니다.", Type.PAYMENT, null);

            acknowledgment.acknowledge();
        } catch (Exception e) {
            System.err.println("Error processing payment cancel fail message: " + e.getMessage());
        }
    }
}
