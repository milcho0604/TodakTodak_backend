package com.padaks.todaktodak.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.padaks.todaktodak.child.domain.Child;
import com.padaks.todaktodak.child.repository.ChildRepository;
import com.padaks.todaktodak.notification.dto.*;
import com.padaks.todaktodak.common.exception.BaseException;
import com.padaks.todaktodak.notification.domain.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import static com.padaks.todaktodak.common.exception.exceptionType.MemberExceptionType.CHILD_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {
    private final FcmService fcmService;
    private final ChildRepository childRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "community-success", containerFactory = "communityKafkaListenerContainerFactory")
    public void consumerNotification(String message, Acknowledgment acknowledgment){
        try {
            CommentSuccessDto dto = objectMapper.readValue(message, CommentSuccessDto.class);

            String category = "";
            if (dto.getType().equals(Type.POST)){
                category = "질문 알림";
                fcmService.sendMessage(dto.getReceiverEmail(), category, dto.getTitle()+"에 대한 답변이 작성되었습니다.",  Type.POST, dto.getPostId());

            }else if (dto.getType().equals(Type.COMMENT)){
                category = "답변 알림";
                String comment = "에 작성한 답변";
                fcmService.sendMessage(dto.getReceiverEmail(), category, dto.getTitle()+comment+"에 대한 답변이 작성되었습니다.",  Type.COMMENT, dto.getPostId());
            }
            acknowledgment.acknowledge();
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "scheduled-reservation-success-notify", containerFactory = "reservationKafkaContainerFactory")
    public void scheduledNotification(String message, Acknowledgment acknowledgment){
//        if (message.startsWith("\"") && message.endsWith("\"")) {
//            message = message.substring(1, message.length() -1).replace("\"", "\"");
//            message = message.replace("\\", "");
//        }

        try {
            ReservationSuccessResDto dto =
                    objectMapper.readValue(message, ReservationSuccessResDto.class);

            Child child = childRepository.findById(Long.parseLong(dto.getChildId()))
                            .orElseThrow(() -> new BaseException(CHILD_NOT_FOUND));

            String body = dto.getReservationDate() + " " + child.getName() + " 예약 되었습니다.";
            String title = "";
            if(dto.getReservationType().equals("Scheduled")){
                title = "스케줄예약";
            }

            fcmService.sendMessage(dto.getAdminEmail(),
                     title + "/" + dto.getMedicalItem() + " 예약 안내 ",
                    body,
                    Type.RESERVATION_NOTIFICATION, null);
            acknowledgment.acknowledge();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "immediate-reservation-success-notify", containerFactory = "reservationKafkaContainerFactory")
    public void immediateNotification(String message, Acknowledgment acknowledgment){

//        if (message.startsWith("\"") && message.endsWith("\"")) {
//            message = message.substring(1, message.length() -1).replace("\"", "\"");
//            message = message.replace("\\", "");
//        }

        try {
            ReservationSuccessResDto dto =
                    objectMapper.readValue(message, ReservationSuccessResDto.class);

            Child child = childRepository.findById(Long.parseLong(dto.getChildId()))
                    .orElseThrow(() -> new BaseException(CHILD_NOT_FOUND));

            String body = dto.getReservationDate() + " " + child.getName() + " 예약 되었습니다.";

            String title = "";
            if(dto.getReservationType().equals("Immediate")){
                title = "바로대기";
            }

            fcmService.sendMessage(dto.getAdminEmail(),
                    title+" : " + dto.getMedicalItem() + " 예약 안내 ",
                    body,
                    Type.RESERVATION_NOTIFICATION, null);
            acknowledgment.acknowledge();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
  
    @KafkaListener(topics = "reservation-before-notify", containerFactory = "reservationKafkaContainerFactory")
    public void reserveBeforeNotify(String message, Acknowledgment acknowledgment){
//        if (message.startsWith("\"") && message.endsWith("\"")) {
//            message = message.substring(1, message.length() -1).replace("\"", "\"");
//            message = message.replace("\\", "");
//        }

        try {
            ReserveBeforeNotifyResDto dto =
                    objectMapper.readValue(message, ReserveBeforeNotifyResDto.class);

            String body = "병원\t\t: " + dto.getHospitalName() +
                    "\n 의사명\t\t: " + dto.getDoctorName() +
                    "\n 예약시간\t: " + dto.getReservationTime();

            fcmService.sendMessage(dto.getMemberEmail() ,
                    "금일 " + dto.getMessage(),
                    body,
                    Type.RESERVATION_NOTIFICATION, null);
            acknowledgment.acknowledge();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // 자녀 공유 알림
    @KafkaListener(topics = "child-share", containerFactory = "childKafkaListenerContainerFactory")
    public void childSuccess(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
            ChildSuccessResDto childSuccessResDto = objectMapper.readValue(message, ChildSuccessResDto.class);
//            System.out.println("Received Child Success DTO: " + childSuccessResDto);

            fcmService.sendMessage(childSuccessResDto.getMemberEmail(), childSuccessResDto.getChildName() + "자녀가 공유되었습니다.",
                    childSuccessResDto.getSharer()+ "님이 " + childSuccessResDto.getChildName()+"님을 공유했습니다.", Type.CHILD, null);

            acknowledgment.acknowledge();
        } catch (Exception e) {
            System.err.println("Error processing child success message: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "payment-success", containerFactory = "payKafkaListenerContainerFactory")
    public void listenPaymentSuccess(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
//            String parsedMessage = message.replaceAll("\\\\", "");  // 백슬래시 제거
//            parsedMessage = parsedMessage.substring(1, parsedMessage.length() - 1);  // 양쪽의 큰 따옴표 제거

            PaymentSuccessDto paymentSuccessDto = objectMapper.readValue(message, PaymentSuccessDto.class);

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

    @KafkaListener(topics = "payment-fail", containerFactory = "payKafkaListenerContainerFactory")
    public void listenPaymentFail(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
//            String parsedMessage = message.replaceAll("\\\\", "");  // 백슬래시 제거
//            parsedMessage = parsedMessage.substring(1, parsedMessage.length() - 1);  // 양쪽의 큰 따옴표 제거

//            PaymentFailDto paymentFailDto = objectMapper.readValue(parsedMessage, PaymentFailDto.class);
            PaymentFailDto paymentFailDto = objectMapper.readValue(message, PaymentFailDto.class);
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

    @KafkaListener(topics = "payment-cancel", containerFactory = "payKafkaListenerContainerFactory")
    public void listenPaymentCancel(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
//            String parsedMessage = message.replaceAll("\\\\", "");  // 백슬래시 제거
//            parsedMessage = parsedMessage.substring(1, parsedMessage.length() - 1);  // 양쪽의 큰 따옴표 제거

            PaymentCancelDto paymentCancelDto = objectMapper.readValue(message, PaymentCancelDto.class);
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

    @KafkaListener(topics = "payment-cancel-fail", containerFactory = "payKafkaListenerContainerFactory")
    public void listenPaymentCancelFail(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
            PaymentCancelFailDto paymentCancelFailDto = objectMapper.readValue(message, PaymentCancelFailDto.class);
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
