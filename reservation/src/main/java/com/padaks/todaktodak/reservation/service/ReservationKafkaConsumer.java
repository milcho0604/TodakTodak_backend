package com.padaks.todaktodak.reservation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.padaks.todaktodak.common.dto.DtoMapper;
import com.padaks.todaktodak.common.exception.BaseException;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.dto.RedisDto;
import com.padaks.todaktodak.reservation.dto.ReservationSaveReqDto;
import com.padaks.todaktodak.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import retrofit2.http.Header;

import java.util.Set;

import static com.padaks.todaktodak.common.exception.exceptionType.ReservationExceptionType.TOOMANY_RESERVATION;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationKafkaConsumer {

    private final RedisTemplate<String, Object> redisTemplate;
    private final DtoMapper dtoMapper;
    private final ReservationRepository reservationRepository;

// 레디스의 동작 여부를 감지
    private boolean isRedisAvailable(){
        try {
            // Redis에 간단한 ping 요청을 보내서 상태를 확인
            return redisTemplate.getConnectionFactory().getConnection().ping().equals("PONG");
        } catch (Exception e) {
            return false; // Redis가 꺼져 있거나 연결이 실패한 경우
        }
    }

    @KafkaListener(topics = "reservationImmediate", groupId = "group_id", containerFactory = "kafkaListenerContainerFactory")
    public void consumerReservation(String message,
                                    @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String hospitalKey,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) String partition,
                                    Acknowledgment acknowledgment) {
        log.info("ReservationConsumer[consumerReservation] : Kafka 메시지 수신 - 병원 파티션 {}, 의사 {}", hospitalKey, partition);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ReservationSaveReqDto dto = objectMapper.readValue(message, ReservationSaveReqDto.class);
            String key = dto.getHospitalId() + ":" + dto.getDoctorEmail();
            if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
                Set<Object> set = redisTemplate.opsForZSet().range(key, 0, -1);
                if (set.size() > 30) {
                    throw new BaseException(TOOMANY_RESERVATION);
                }
            }
            String sequenceKey = "sequence" + ":" +dto.getHospitalId();
            Long sequence = redisTemplate.opsForValue().increment(sequenceKey, 1);

            Reservation reservation = dtoMapper.toReservation(dto);
            reservationRepository.save(reservation);
            RedisDto redisDto = dtoMapper.toRedisDto(reservation);

            redisTemplate.opsForZSet().add(key, redisDto, sequence);
            acknowledgment.acknowledge();
            log.info("KafkaListener[handleReservation] : 예약 대기열 처리 완료");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
