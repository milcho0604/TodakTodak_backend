package com.padaks.todaktodak.payment.controller;

import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.payment.dto.MemberPayDto;
import com.padaks.todaktodak.payment.dto.PaymentListResDto;
import com.padaks.todaktodak.payment.dto.PaymentReqDto;
import com.padaks.todaktodak.payment.service.PaymentService;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            MemberPayDto memberDto = paymentService.getMemberInfo();
            return ResponseEntity.ok(memberDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
    }

    // 단건 결제 요청 처리
    @PostMapping("/single")
    public ResponseEntity<?> processPayment(@RequestBody String impUid) {
        try {
            System.out.println("Received impUid: " + impUid);
            PaymentReqDto paymentDto = paymentService.processPayment(impUid);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "결제 성공", paymentDto.getImpUid()));
//            return ResponseEntity.ok(paymentDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResDto(HttpStatus.BAD_REQUEST, "결제 실패: " + e.getMessage(), null));
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 결제 취소 처리
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelPayment(@RequestBody String impUid) {
        try {
            IamportResponse<Payment> cancelResponse = paymentService.cancelPayment(impUid);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK,"결제 취소 성공: " + cancelResponse.getResponse().getImpUid(), impUid));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResDto(HttpStatus.BAD_REQUEST,"결제 취소 실패: " + e.getMessage(), null));
        }
    }

    // 결제 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<?> paymentList(Pageable pageable){
        Page<PaymentListResDto> paymentList = paymentService.paymentList(pageable);
        CommonResDto dto = new CommonResDto(HttpStatus.OK,"결제내역을 조회합니다..", paymentList);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/sub")
    public ResponseEntity<String> processSubscription() {
        try {
            paymentService.processSubscriptionPayments();
            return ResponseEntity.ok("정기 결제 처리 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("정기 결제 처리 중 오류 발생");
        }
    }


}
