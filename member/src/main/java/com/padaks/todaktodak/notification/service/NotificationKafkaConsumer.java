package com.padaks.todaktodak.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.padaks.todaktodak.child.domain.Child;
import com.padaks.todaktodak.child.repository.ChildRepository;
import com.padaks.todaktodak.notification.dto.*;
import com.padaks.todaktodak.common.exception.BaseException;
import com.padaks.todaktodak.member.repository.MemberRepository;
import com.padaks.todaktodak.notification.domain.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import static com.padaks.todaktodak.common.exception.exceptionType.MemberExceptionType.CHILD_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationKafkaConsumer {
    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberRepository memberRepository;
    private final FcmService fcmService;
    private final ChildRepository childRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "community-success", groupId = "group_id", containerFactory = "kafkaListenerContainerFactory")
    public void consumerNotification(String message, Acknowledgment acknowledgment){
        if (message.startsWith("\"") && message.endsWith("\"")) {
            message = message.substring(1, message.length() -1).replace("\"", "\"");
            message = message.replace("\\", "");
        }
        try {
            CommentSuccessDto dto = objectMapper.readValue(message, CommentSuccessDto.class);

            // 처음에 dto에서 email로 멤버 객체를 찾아서 멤버 객체에서 memberId를 추출하여 fcmService로 멤버 id를 건내어
            // findById로 member객체를 다시 조립하여 해당 멤버에게 메시지를 전송하는 로직을
            // dto에 있던 email을 보내어 fcmService에서 멤버 객체를 조립하는 방식으로 수정
//            Member member = memberRepository.findByMemberEmail(dto.getReceiverEmail()).orElseThrow(()->new EntityNotFoundException("존재하지 않는 회원입니다."));

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
        if (message.startsWith("\"") && message.endsWith("\"")) {
            message = message.substring(1, message.length() -1).replace("\"", "\"");
            message = message.replace("\\", "");
        }

        try {
            ReservationSuccessResDto dto =
                    objectMapper.readValue(message, ReservationSuccessResDto.class);

            Child child = childRepository.findById(Long.parseLong(dto.getChildId()))
                            .orElseThrow(() -> new BaseException(CHILD_NOT_FOUND));

            String body = "\n 예약일자\t: " + dto.getReservationDate() +
                    "\n예약시간\t: " + dto.getReservationTime() +
                    "\n의사\t\t: " + dto.getDoctorName() +
                    "\n예약자\t\t: " + dto.getMemberName() +
                    "\n자녀이름\t: " + child.getName();

            fcmService.sendMessage(dto.getAdminEmail(),
                    "# " + dto.getReservationType()+"/" + dto.getMedicalItem() + " 예약 안내 #",
                    body,
                    Type.RESERVATION_NOTIFICATION, null);
            acknowledgment.acknowledge();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "immediate-reservation-success-notify", containerFactory = "reservationKafkaContainerFactory")
    public void immediateNotification(String message, Acknowledgment acknowledgment){

        if (message.startsWith("\"") && message.endsWith("\"")) {
            message = message.substring(1, message.length() -1).replace("\"", "\"");
            message = message.replace("\\", "");
        }

        try {
            ReservationSuccessResDto dto =
                    objectMapper.readValue(message, ReservationSuccessResDto.class);

            Child child = childRepository.findById(Long.parseLong(dto.getChildId()))
                    .orElseThrow(() -> new BaseException(CHILD_NOT_FOUND));

            String body = "예약일자\t: " + dto.getReservationDate() +
                    "\n 의사\t\t: " + dto.getDoctorName() +
                    "\n 예약자\t\t: " + dto.getMemberName() +
                    "\n 자녀이름\t: " + child.getName();
            fcmService.sendMessage(dto.getAdminEmail(),
                    "# " + dto.getReservationType()+"/" + dto.getMedicalItem() + " 예약 안내 #",
                    body,
                    Type.RESERVATION_NOTIFICATION, null);
            acknowledgment.acknowledge();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
  
    @KafkaListener(topics = "reservation-before-notify", containerFactory = "reservationKafkaContainerFactory")
    public void reserveBeforeNotify(String message, Acknowledgment acknowledgment){
//        ObjectMapper objectMapper = new ObjectMapper();
        if (message.startsWith("\"") && message.endsWith("\"")) {
            message = message.substring(1, message.length() -1).replace("\"", "\"");
            message = message.replace("\\", "");
        }

        try {
            ReserveBeforeNotifyResDto dto =
                    objectMapper.readValue(message, ReserveBeforeNotifyResDto.class);

            String body = "병원\t\t: " + dto.getHospitalName() +
                    "\n 의사명\t\t: " + dto.getDoctorName() +
                    "\n 예약시간\t: " + dto.getReservationTime();

            fcmService.sendMessage(dto.getMemberEmail() ,
                    "# 금일 " + dto.getMessage() + " #",
                    body,
                    Type.RESERVATION_NOTIFICATION, null);
            acknowledgment.acknowledge();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // 자녀 공유 알림
    @KafkaListener(topics = "child-share", groupId = "child-group", containerFactory = "childKafkaListenerContainerFactory")
    public void listenPaymentSuccess(String message, Acknowledgment acknowledgment) throws JsonProcessingException {
        try {
            System.out.println(message);
            ChildSuccessResDto childSuccessResDto = objectMapper.readValue(message, ChildSuccessResDto.class);
            System.out.println("Received Child Success DTO: " + childSuccessResDto);

            fcmService.sendMessage(childSuccessResDto.getMemberEmail(), childSuccessResDto.getChildName() + "자녀가 공유되었습니다.",
                    childSuccessResDto.getSharer()+ "님이 " + childSuccessResDto.getChildName()+"님을 공유했습니다.", Type.CHILD, null);

            acknowledgment.acknowledge();
        } catch (Exception e) {
            System.err.println("Error processing payment success message: " + e.getMessage());
        }
    }

}
