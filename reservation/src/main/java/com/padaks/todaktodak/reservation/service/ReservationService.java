package com.padaks.todaktodak.reservation.service;

import com.padaks.todaktodak.chatroom.service.UntactChatRoomService;
import com.padaks.todaktodak.common.dto.DtoMapper;
import com.padaks.todaktodak.common.exception.BaseException;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.domain.ReserveType;
import com.padaks.todaktodak.reservation.dto.*;
import com.padaks.todaktodak.reservation.domain.ReservationHistory;
import com.padaks.todaktodak.reservation.domain.Status;
import com.padaks.todaktodak.reservation.repository.ReservationHistoryRepository;
import com.padaks.todaktodak.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.padaks.todaktodak.common.exception.exceptionType.ReservationExceptionType.*;
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationHistoryRepository reservationHistoryRepository;
    private final DtoMapper dtoMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String RESERVATION_LIST_KEY = "doctor_list";
    private final MemberFeign memberFeign;
    private final UntactChatRoomService untactChatRoomService;

//    진료 미리 예약 기능
    public Reservation scheduleReservation(ReservationSaveReqDto dto){
        log.info("ReservationService[scheduleReservation] : 시작");
//        진료 예약 시 해당 의사 선생님의 예약이 존재할 경우 Exception을 발생 시키기 위한 코드
        reservationRepository.findByDoctorEmailAndReservationDateAndReservationTime
                (dto.getDoctorEmail(), dto.getReservationDate(), dto.getReservationTime())
                .ifPresent(reservation -> {
                    throw new BaseException(RESERVATION_DUPLICATE);
                });
        Reservation reservation = dtoMapper.toReservation(dto);
        Reservation savedReservation = reservationRepository.save(reservation);
        sendReservationNotification(savedReservation);
        return savedReservation;
    }

//    당일 예약 기능
    public Reservation immediateReservation(ReservationSaveReqDto dto){
        log.info("KafkaListener[handleReservation] : 예약 요청 처리 시작");
//        doctor 이메일로 찾아서 id로 바꿔주면 됨
        String key = RESERVATION_LIST_KEY + dto.getHospitalId();
        // Redis에서 예약 개수 확인 (대기열 관리)
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            Set<Object> set = redisTemplate.opsForZSet().range(key, 0, -1);
            if (set.size() > 30) {
                throw new BaseException(TOOMANY_RESERVATION);
            }
        }
        kafkaTemplate.send("reservationTopic", dto);

        String sequenceKey = "sequence" + dto.getHospitalId();
        Long sequence = redisTemplate.opsForValue().increment(sequenceKey, 1);

        // 예약 저장
        Reservation reservation = dtoMapper.toReservation(dto);
        reservationRepository.save(reservation);
        if (reservation.isUntact()) {
            untactChatRoomService.untactCreate(reservation);
        }
        RedisDto redisDto = dtoMapper.toRedisDto(reservation);
        redisTemplate.opsForZSet().add(key, redisDto, sequence);

        log.info("KafkaListener[handleReservation] : 예약 처리 완료");

        return reservation;

    }

//    예약 취소 기능
    public void cancelledReservation(Long id){
        log.info("ReservationSErvice[cancelledRservation] : 시작");
//        예약의 id 로 찾고 만약 예약이 없을경우 RESERVATION_NOT_FOUND 예외를 발생 -> BaseException 에 정의
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new BaseException(RESERVATION_NOT_FOUND));
//        hard delete 로 DB 상에서 완전히 지워버림
        reservationRepository.delete(reservation);
//        reservationHistory 테이블에 저장하기 위한 코드
        ReservationHistory reservationHistory = dtoMapper.toReservationHistory(reservation);
        reservationHistory.setStatus(Status.Cancelled);
//        reservationHistory 테이블에 저장.
        reservationHistoryRepository.save(reservationHistory);

        //        Redis의 예약 찾기
        String key = RESERVATION_LIST_KEY+reservation.getHospitalId();
        RedisDto redisDto = dtoMapper.toRedisDto(reservation);
//        list 에서 해당 예약을 삭제
        redisTemplate.opsForZSet().remove(key, redisDto);
    }
    
//    예약 조회 기능
    public List<?> checkListReservation(CheckListReservationResDto resDto, Pageable pageable){
//        feign 으로 연결 되면 여기에 email 로 해당 user 찾는 로직이 들어갈 예정
        
//        여기서 페이징 처리할 예정 -> 페이징 처리하면서 예약
//        여기서 미리 예약 , 당일 예약 분기처리도 해줄 예정
        Page<Reservation> reservationPage;
        if(resDto.getType().toString().equals("All")){
            reservationPage = reservationRepository.findByMemberEmail(pageable, resDto.getEmail());
        }
        else{
            reservationPage = reservationRepository
                    .findByMemberEmailAndReservationType(
                            pageable,
                            resDto.getEmail(),
                            dtoMapper.resTypeToReserveType(resDto.getType()));
        }


        List<CheckListReservationReqDto> dto = reservationPage.stream()
                .map(dtoMapper::toListReservation)
                .collect(Collectors.toList());

        return dto;
    }

//    예약 접수 기능 (병원 admin의 예약 상태 변경)
    public Reservation receipt(UpdateStatusReservation updateStatusReservation) {
        Reservation reservation = reservationRepository.findById(updateStatusReservation.getId())
                .orElseThrow(() -> new BaseException(RESERVATION_NOT_FOUND));
//       예약의 상태를 completed로 변경한다
        reservation.updateStatus(updateStatusReservation.getStatus());
        return reservation;
    }

    public void sendReservationNotification(Reservation reservation) {
        MemberResDto member = memberFeign.getMemberByEmail(reservation.getMemberEmail());
        String content = String.format("환자 %s님이 %s에 %s의 예약을 했습니다.",
                member.getName(),
                reservation.getReservationDate().toString(),
                reservation.getReservationTime().toString()
        );
//        알림 받는 사람 병원 admin Email로 변경해야함
        NotificationReqDto notificationReqDto = NotificationReqDto.builder()
                .memberEmail(reservation.getDoctorEmail())
                .type("RESERVATION_NOTIFICATION")
                .content(content)
                .refId(reservation.getId())
                .build();

        memberFeign.sendReservationNotification(notificationReqDto);
    }
}
