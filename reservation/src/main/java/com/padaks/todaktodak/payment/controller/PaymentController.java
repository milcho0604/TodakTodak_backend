package com.padaks.todaktodak.payment.controller;

import com.padaks.todaktodak.payment.dto.PaymentDto;
import com.padaks.todaktodak.payment.service.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.GET;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public String showPaymentPage() {
        return "pay"; // pay.html 파일을 반환
    }
    // 결제 요청 처리
    @PostMapping("/process")
    public ResponseEntity<PaymentDto> processPayment(@RequestParam(defaultValue = "imp86026232")String impUid) {
        try {
            // impUid를 이용해 결제 처리
            PaymentDto paymentDto = paymentService.processPayment(impUid);
            return ResponseEntity.ok(paymentDto);  // 처리된 결제 정보를 반환
        } catch (Exception e) {
            // 결제 처리 중 오류 발생 시 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 결제 취소 처리
    @PostMapping("/cancel")
    public ResponseEntity<String> cancelPayment(@RequestParam String impUid) {
        try {
            // impUid를 이용해 결제 취소
            IamportResponse<Payment> cancelResponse = paymentService.cancelPayment(impUid);
            return ResponseEntity.ok("결제 취소 성공: " + cancelResponse.getResponse().getImpUid());
        } catch (Exception e) {
            // 결제 취소 중 오류 발생 시 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 취소 실패: " + e.getMessage());
        }
    }

}
