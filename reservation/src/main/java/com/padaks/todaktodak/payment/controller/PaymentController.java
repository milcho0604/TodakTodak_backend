package com.padaks.todaktodak.payment.controller;

import com.padaks.todaktodak.payment.dto.MemberDto;
import com.padaks.todaktodak.payment.dto.PaymentReqDto;
import com.padaks.todaktodak.payment.service.PaymentService;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/get/member")
    private ResponseEntity<?> getMemberTest(){
        try {
            MemberDto memberDto = paymentService.getMemberInfo();
            return ResponseEntity.ok(memberDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
    }

    // 결제 요청 처리
    @PostMapping("/process")
    public ResponseEntity<PaymentReqDto> processPayment(@RequestBody String impUid) {
        try {
            System.out.println("Received impUid: " + impUid);
            PaymentReqDto paymentDto = paymentService.processPayment(impUid);
            return ResponseEntity.ok(paymentDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 결제 취소 처리
    @PostMapping("/cancel")
    public ResponseEntity<String> cancelPayment(@RequestParam String impUid) {
        try {
            IamportResponse<Payment> cancelResponse = paymentService.cancelPayment(impUid);
            return ResponseEntity.ok("결제 취소 성공: " + cancelResponse.getResponse().getImpUid());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 취소 실패: " + e.getMessage());
        }
    }
}
