package com.padaks.todaktodak.reservation.service;

import com.padaks.todaktodak.common.dto.DtoMapper;
import com.padaks.todaktodak.common.exception.BaseException;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.dto.CheckHospitalListReservationReqDto;
import com.padaks.todaktodak.reservation.dto.RedisDto;
import com.padaks.todaktodak.reservation.dto.UpdateStatusReservation;
import com.padaks.todaktodak.reservation.realtime.RealTimeService;
import com.padaks.todaktodak.reservation.realtime.WaitingTurnDto;
import com.padaks.todaktodak.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.padaks.todaktodak.common.exception.exceptionType.ReservationExceptionType.RESERVATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReservationAdminService {

    private final ReservationRepository reservationRepository;
    private final DtoMapper dtoMapper;

    private final RedisTemplate<String, Object> redisTemplate;
    private final RealTimeService realTimeService;

    private static final String RESERVATION_LIST_KEY = "doctor_list";

    public List<?> checkListReservation(
            CheckHospitalListReservationReqDto reqDto,
            Pageable pageable){

        Page<Reservation> reservationPage;
//        유저별
        if(reqDto.getMemberEmail() != null){
            reservationPage = reservationRepository.findByMemberEmail(pageable, reqDto.getMemberEmail());
        }
//        의사별
        else if (reqDto.getDoctorEmail() != null) {
            reservationPage = reservationRepository.findByDoctorEmail(pageable, reqDto.getDoctorEmail());
        }
//        예약 상태별
        else{
            reservationPage = reservationRepository.findByStatus(pageable, reqDto.getStatus());
        }

        return reservationPage.stream()
                .map(dtoMapper::toHospitalListReservation)
                .collect(Collectors.toList());
    }

    public void statusReservation(UpdateStatusReservation updateStatusReservation){
        Reservation reservation = reservationRepository.findById(updateStatusReservation.getId())
                .orElseThrow(() -> new BaseException(RESERVATION_NOT_FOUND));
        reservation.updateStatus(updateStatusReservation.getStatus());

//        Redis의 예약 찾기
        String key = reservation.getHospital().getId() + ":"+ reservation.getDoctorEmail();
        RedisDto redisDto = dtoMapper.toRedisDto(reservation);
//        list 에서 해당 예약을 삭제
        redisTemplate.opsForZSet().remove(key, redisDto);
        realTimeService.delete(reservation.getHospital().getName(), reservation.getDoctorName(), redisDto.getId().toString());
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void flushDb(){
        redisTemplate.getConnectionFactory().getConnection().flushDb();
        log.info("금일 redis 초기화 완료");
    }
}
