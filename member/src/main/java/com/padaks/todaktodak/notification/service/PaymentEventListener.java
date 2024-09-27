package com.padaks.todaktodak.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.padaks.todaktodak.notification.dto.PaymentSuccessDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventListener {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "payment-success", groupId = "payment-group")
    public void listenPaymentSuccess(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        PaymentSuccessDto paymentSuccessDto = objectMapper.readValue(message, PaymentSuccessDto.class);

        // 변환된 DTO 출력
        System.out.println("Received Payment Success DTO: " + paymentSuccessDto);

        // 결제 성공 후 알림을 보낼 로직

        // 오프셋 변경
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "payment-fail", groupId = "payment-group")
    public void listenPaymentFail(String message, Acknowledgment acknowledgment) {
        System.out.println("Received Payment Fail message: " + message);
        // 결제 실패 후 알림을 보낼 로직


        // 오프셋 변경
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = "payment-cancel", groupId = "payment-group")
    public void listenPaymentCancel(String message, Acknowledgment acknowledgment) {
        System.out.println("Received Payment Cancel message: " + message);
        // 결제 취소 후 알림을 보낼 로직


        // 오프셋 변경
        acknowledgment.acknowledge();
    }
}
