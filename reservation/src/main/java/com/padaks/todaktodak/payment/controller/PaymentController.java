package com.padaks.todaktodak.payment.controller;

import com.padaks.todaktodak.common.dto.CommonResDto;
import com.padaks.todaktodak.common.dto.MemberFeignDto;
import com.padaks.todaktodak.payment.domain.PaymentMethod;
import com.padaks.todaktodak.payment.dto.PaymentMemberResDto;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    // 단건 결제 멤버 요청
    @GetMapping("/get/member")
    public ResponseEntity<?> getMemberTest(){
        try {
            MemberFeignDto memberDto = paymentService.getMemberInfo();
            return ResponseEntity.ok(memberDto);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 정기 결제 멤버 요청
    @GetMapping("/get/hospital")
    public ResponseEntity<?> subHospital(){
        try {
            PaymentMemberResDto paymentMemberResDto = paymentService.paymentMemberResDto();
            return ResponseEntity.ok(paymentMemberResDto);
        } catch (Exception e) {
            // 예외 발생 시 로그 추가
            log.error("Error fetching hospital data", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 단건 결제 요청 처리
    @PostMapping("/single")
    public ResponseEntity<?> processPayment(@RequestBody String impUid) {
        try {
            System.out.println("Received impUid: " + impUid);
            PaymentReqDto paymentDto = paymentService.processSinglePayment(impUid);
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

    @GetMapping("/detail/list")
    public ResponseEntity<Page<PaymentListResDto>> paymentListSearch(
            @RequestParam(required = false) String query, // 검색어 (impUid나 memberEmail에 포함될 수 있음)
            @RequestParam(required = false) PaymentMethod paymentMethod, // 필터링용 결제 방식 (정기, 단건)
            Pageable pageable) {

        // 결제 리스트를 검색어와 필터링으로 조회
        Page<PaymentListResDto> paymentList = paymentService.paymentListSearch(query, paymentMethod, pageable);

        return ResponseEntity.ok(paymentList);
    }

    // 정기 결제 요청
    @PostMapping("/sub")
    public ResponseEntity<?> subscriptionPayment(@RequestBody String impUid) {
        try {
            PaymentReqDto paymentDto = paymentService.processSubscriptionPayment(impUid);
            return ResponseEntity.ok(paymentDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("정기 결제 처리 실패: " + e.getMessage());
        }
    }

//    @Scheduled(cron = "0 0/1 * * * ?")  // 매 2분마다 실행 (테스트용)
    @Scheduled(cron = "0 0 * * * ?")  // 매 1시간마다 실행
    public void processMonthlySubscriptions() {
        log.info("정기 결제 스케줄러 시작");  // 스케줄러가 실행되었는지 확인
        paymentService.processSubscriptions();
    }

    @PostMapping("/test-subscription")
    public ResponseEntity<?> testSubscription() {
        paymentService.processSubscriptions();  // 스케줄러 대신 수동으로 정기 결제 실행
        return ResponseEntity.ok("정기 결제 테스트 완료");
    }

    @PostMapping("/subCancel")
    public ResponseEntity<?> cancleSub(@RequestBody String impUid){
        try {
            IamportResponse<Payment> cancleResponse = paymentService.cancelSubscription(impUid);
            return ResponseEntity.ok(new CommonResDto(HttpStatus.OK, "구독 취소 성공", cancleResponse.getResponse().getImpUid()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResDto(HttpStatus.BAD_REQUEST,"구독 취소 실패: " + e.getMessage(), null));
        }
    }

    @GetMapping("/get/fee")
    public ResponseEntity<?> getMedicalChartFee(){
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            int fee = 0;
            if(email.equals(paymentService.medicalChart.getReservation().getMemberEmail())){
                fee = paymentService.medicalChart.getFee();
            }
            System.out.println(fee);
            return ResponseEntity.ok(fee);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/get/reservation/{id}")
    public ResponseEntity<?> getReservationId(@PathVariable Long id){
        try {
            int fee = paymentService.medicalChart(id).getFee();
            return ResponseEntity.ok(fee);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
