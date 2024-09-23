package com.padaks.todaktodak.payment.service;

import com.padaks.todaktodak.payment.domain.Pay;
import com.padaks.todaktodak.payment.domain.PaymentMethod;
import com.padaks.todaktodak.payment.dto.MemberDto;
import com.padaks.todaktodak.payment.dto.PaymentDto;
import com.padaks.todaktodak.payment.dto.PaymentSuccessDto;
import com.padaks.todaktodak.payment.domain.PaymentStatus;
import com.padaks.todaktodak.payment.repository.PaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
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

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PaymentService {

//    private final MemberFeignClient memberFeignClient;
    private final IamportClient iamportClient;
    private final PaymentRepository paymentRepository;

//    // member 객체 리턴
//    public MemberDto getMemberInfo() {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String token = (String) authentication.getCredentials();
//        System.out.println(token);
//        MemberDto member = memberFeignClient.getMemberEmail(token);  // Feign Client로부터 MemberDTO 받아옴
//        return member;
//    }

//    // 결제 로직 구현
//    public PaymentDto processPayment() {
//        MemberDto member = getMemberInfo();  // 현재 로그인한 사용자 정보
//
//        try {
//            // 결제 요청 처리 (100원 결제)
//            String impUid = createIamportPayment(member.getMemberEmail(), 100);
//
//            // Pay 엔티티 생성 후 저장
//            Pay pay = Pay.builder()
//                    .memberEmail(member.getMemberEmail())
//                    .impUid(impUid)
//                    .amount(100)
//                    .paymentStatus(PaymentStatus.READY)
//                    .paymentMethod(PaymentMethod.CARD)
//                    .requestTimeStamp(LocalDateTime.now())
//                    .build();
//
//            // 저장 로직 추가 (아직 리포지토리 연결이 안 되어 있다면 주석 처리)
//             paymentRepository.save(pay);
//
//            // 성공적으로 결제된 PaymentDto 반환
//            return PaymentDto.builder()
//                    .id(pay.getId())
//                    .memberEmail(pay.getMemberEmail())
//                    .impUid(pay.getImpUid())
//                    .amount(pay.getAmount())
//                    .paymentStatus(pay.getPaymentStatus().toString())
//                    .paymentMethod(pay.getPaymentMethod().toString())
//                    .requestTimeStamp(pay.getRequestTimeStamp())
//                    .build();
//        } catch (Exception e) {
//            log.error("결제 처리 중 오류 발생: ", e);
//            throw new RuntimeException("결제 처리 실패", e);
//        }
//    }
//
//    private String createIamportPayment(String memberEmail, int amount) throws Exception {
//        // 아임포트 API 호출로 결제 요청 (단순화된 결제 요청 로직)
//        IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid("imp_dummy_uid"); // 임시 UID 사용
//
//        // API 호출 성공 여부 확인
//        if (paymentResponse.getResponse() == null) {
//            throw new Exception("결제 요청 실패: " + paymentResponse.getMessage()); // 실패 시 메시지 반환
//        }
//
//        // 결제 금액 불일치 확인 -> 여기서 Amount는 int가 아니기 때문에 형변환이 필요
//        if (paymentResponse.getResponse().getAmount().intValue() != amount) {
//            throw new Exception("결제 금액 불일치");
//        }
//
//        // 결제 성공 시 결제 고유 번호 반환
//        return paymentResponse.getResponse().getImpUid();
//    }

    // 결제 로직 구현
    public PaymentDto processPayment(String impUid) {  // 클라이언트로부터 받은 impUid
//        MemberDto member = getMemberInfo();  // 현재 로그인한 사용자 정보
        String memberEmail = "abc@test.com";
        System.out.println("UID는 지금부터입니다.");
        System.out.println(impUid);

        try {
            // impUid를 통해 결제 정보 확인
            IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(impUid);

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
                    .memberEmail(memberEmail)
                    .impUid(impUid)
                    .amount(100)
                    .paymentStatus(PaymentStatus.OK)  // 결제 완료로 상태 업데이트
                    .paymentMethod(PaymentMethod.CARD)
                    .requestTimeStamp(LocalDateTime.now())
                    .approvalTimeStamp(LocalDateTime.now())  // 결제 승인 시간
                    .build();

            // 저장 로직
            paymentRepository.save(pay);

            // 성공적으로 결제된 PaymentDto 반환
            return PaymentDto.builder()
                    .id(pay.getId())
                    .memberEmail(pay.getMemberEmail())
                    .impUid(pay.getImpUid())
                    .amount(pay.getAmount())
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
        // 취소 데이터를 생성 (impUid로 취소 요청)
        CancelData cancelData = new CancelData(impUid, true);  // impUid와 함께 결제 취소를 요청합니다.

        // Iamport API를 사용해 결제 취소 요청
        IamportResponse<Payment> cancelResponse = iamportClient.cancelPaymentByImpUid(cancelData);

        // 취소 결과 확인
        if (cancelResponse.getResponse() != null) {
            log.info("결제 취소 성공: {}", cancelResponse.getResponse());
        } else {
            log.error("결제 취소 실패: {}", cancelResponse.getMessage());
            throw new Exception("결제 취소 실패: " + cancelResponse.getMessage());
        }
        return cancelResponse;
    }
}
