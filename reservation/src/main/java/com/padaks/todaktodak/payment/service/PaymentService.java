package com.padaks.todaktodak.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.padaks.todaktodak.payment.domain.Pay;
import com.padaks.todaktodak.payment.domain.PaymentMethod;
import com.padaks.todaktodak.payment.dto.MemberDto;
import com.padaks.todaktodak.payment.dto.PaymentReqDto;
import com.padaks.todaktodak.payment.domain.PaymentStatus;
import com.padaks.todaktodak.payment.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PaymentService {

    private final MemberFeignClient memberFeignClient;
    private final IamportClient iamportClient;
    private final PaymentRepository paymentRepository;

    // member 객체 리턴, 토큰 포함
    public MemberDto getMemberInfo() {
        MemberDto member = memberFeignClient.getMemberEmail();  // Feign Client에 토큰 추가
//        System.out.println("멤버 디버깅을 위한: " + member);
        return member;
    }

    // 결제 로직 구현
    public PaymentReqDto processPayment(String impUid) {
        MemberDto member = getMemberInfo();  // 현재 로그인한 사용자 정보
        System.out.println(impUid);
        String actualImpUid = extractImpUid(impUid);  // impUid 값 추출 함수 사용
        System.out.println("Extracted impUid: " + actualImpUid);

        try {
            // impUid를 통해 결제 정보 확인
            System.out.println(actualImpUid);
            IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(actualImpUid);

            // 결제 정보가 존재하는지 확인
            if (paymentResponse.getResponse() == null) {
                throw new Exception("결제 요청 실패: " + paymentResponse.getMessage());
            }

            // 결제 금액 및 기타 검증 로직 (여기서는 금액 100원으로 고정)
            if (paymentResponse.getResponse().getAmount().intValue() != 100) {
                throw new Exception("결제 금액 불일치");
            }

            // Pay 엔티티 생성 후 저장
            Pay pay = Pay.builder()
                    .memberEmail(member.getMemberEmail())
                    .impUid(actualImpUid)
                    .amount(100)
                    .buyerName(member.getName())
                    .buyerTel(member.getPhoneNumber())
                    .merchantUid("order_no_" + new Date().getTime())
                    .paymentStatus(PaymentStatus.OK)  // 결제 완료로 상태 업데이트
                    .paymentMethod(PaymentMethod.SINGLE)
                    .requestTimeStamp(LocalDateTime.now())
                    .approvalTimeStamp(LocalDateTime.now())  // 결제 승인 시간
                    .build();

            // 저장 로직
            paymentRepository.save(pay);

            // 성공적으로 결제된 PaymentReqDto 반환
            return PaymentReqDto.builder()
                    .id(pay.getId())
                    .memberEmail(pay.getMemberEmail())
                    .impUid(pay.getImpUid())
                    .amount(pay.getAmount())
                    .merchantUid(pay.getMerchantUid())
                    .buyerName(pay.getBuyerName())
                    .buyerTel(pay.getBuyerTel())
                    .paymentStatus(pay.getPaymentStatus().toString())
                    .paymentMethod(pay.getPaymentMethod().toString())
                    .requestTimeStamp(pay.getRequestTimeStamp())
                    .approvalTimeStamp(pay.getApprovalTimeStamp())
                    .build();

        } catch (Exception e) {
            log.error("결제 처리 중 오류 발생: ", e);
            throw new RuntimeException("결제 처리 실패", e);
        }
    }

    // 결제 취소 메소드
    public IamportResponse<Payment> cancelPayment(String impUid) throws Exception {
        CancelData cancelData = new CancelData(impUid, true);  // impUid와 함께 결제 취소를 요청

        IamportResponse<Payment> cancelResponse = iamportClient.cancelPaymentByImpUid(cancelData);

        if (cancelResponse.getResponse() != null) {
            log.info("결제 취소 성공: {}", cancelResponse.getResponse());
        } else {
            log.error("결제 취소 실패: {}", cancelResponse.getMessage());
            throw new Exception("결제 취소 실패: " + cancelResponse.getMessage());
        }
        return cancelResponse;
    }
    private String extractImpUid(String impUidJson) {
        try {
            // JSON 파싱을 통해 impUid 값 추출 (예: Jackson 사용)
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> map = objectMapper.readValue(impUidJson, Map.class);
            return map.get("impUid");
        } catch (Exception e) {
            throw new RuntimeException("impUid 값을 추출하는 중 오류 발생", e);
        }
    }
}
