package com.padaks.todaktodak.reservation.service;

import com.padaks.todaktodak.common.dto.DtoMapper;
import com.padaks.todaktodak.common.exception.BaseException;

import com.padaks.todaktodak.common.feign.MemberFeign;
import com.padaks.todaktodak.hospital.domain.Hospital;
import com.padaks.todaktodak.hospital.repository.HospitalRepository;
import com.padaks.todaktodak.medicalchart.service.MedicalChartService;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.domain.Status;

import com.padaks.todaktodak.reservation.dto.*;
import com.padaks.todaktodak.reservation.realtime.RealTimeService;
import com.padaks.todaktodak.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.*;
import javax.persistence.EntityNotFoundException;


import java.util.stream.Collectors;

import static com.padaks.todaktodak.common.exception.exceptionType.ReservationExceptionType.RESERVATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@DependsOn("realTimeService")
public class ReservationAdminService {

    private final ReservationRepository reservationRepository;
    private final MemberFeign memberFeign;
    private final DtoMapper dtoMapper;

    private final RedisTemplate<String, Object> redisTemplate;
    private final RealTimeService realTimeService;
    private final MedicalChartService medicalChartService;

    private final HospitalRepository hospitalRepository;

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
            reservationPage = reservationRepository.findByDoctorEmailAndReservationDateAndStatus(
                    reqDto.getDoctorEmail(),
                    reqDto.getDate(),
                    reqDto.getStatus(),
                    pageable);
        }
//        예약 상태별
        else{
            reservationPage = reservationRepository.findByStatus(pageable, reqDto.getStatus());
        }

        List<CheckHospitalListReservationResDto> dtos = new ArrayList<>();
        for(Reservation res : reservationPage){
            ChildResDto childResDto = memberFeign.getMyChild(res.getChildId());
            dtos.add(dtoMapper.toHospitalListReservation(res, childResDto.getName(), childResDto.getSsn()));
        }
        return dtos;
    }

//    해당하는 병원의 당일 예약 리스트
    public List<?> checkImmediateReservationList(HospitalReservationListReqDto dto){
        Hospital hospital = hospitalRepository.findByName(dto.getHospitalName())
                .orElseThrow(() -> new EntityNotFoundException("해당 하는 병원이 없습니다."));

        List<Reservation> reservationList;

        if(dto.getReserveType() != null) {
            reservationList =
                    reservationRepository.findByHospitalAndReservationTypeAndReservationDateAndStatus(
                            hospital,
                            dto.getReserveType(),
                            dto.getDate(),
                            dto.getStatus()
                    );
        }else{
            reservationList =
                    reservationRepository.findByHospitalAndReservationDateAndStatus(
                            hospital,
                            dto.getDate(),
                            dto.getStatus()
                    );
        }
        List<CheckHospitalListReservationResDto> dtos = new ArrayList<>();

        for(Reservation res : reservationList){
            ChildResDto childResDto = memberFeign.getMyChild(res.getChildId());
            CheckHospitalListReservationResDto resDto = dtoMapper.toHospitalListReservation(res, childResDto.getName(), childResDto.getSsn());
            dtoMapper.setReservationTime(resDto, res);
            dtos.add(resDto);
        }

        return dtos;
    }

//    예약 상태 변경 메소드
    @CacheEvict(value = "yesterdayReservations", allEntries = true)
    public void statusReservation(UpdateStatusReservation updateStatusReservation){
        Reservation reservation = reservationRepository.findById(updateStatusReservation.getId())
                .orElseThrow(() -> new BaseException(RESERVATION_NOT_FOUND));
        reservation.updateStatus(updateStatusReservation.getStatus());

        DoctorResDto doctorResDto = memberFeign.getDoctor(reservation.getDoctorEmail());

//        Redis의 예약 찾기
        String key = reservation.getHospital().getId() + ":"+ reservation.getDoctorEmail();
        RedisDto redisDto = dtoMapper.toRedisDto(reservation);
//        list 에서 해당 예약을 삭제
        redisTemplate.opsForZSet().remove(key, redisDto);
        realTimeService.delete(reservation.getHospital().getName(), doctorResDto.getId().toString(), redisDto.getId().toString());
    }

    //  비대면 진료 예약 상태 변경 메소드
    public void updateStatusUntactReservation(UpdateStatusReservation updateStatusReservation){
        Reservation reservation = reservationRepository.findById(updateStatusReservation.getId())
                .orElseThrow(() -> new BaseException(RESERVATION_NOT_FOUND));
        reservation.updateStatus(updateStatusReservation.getStatus());
//         예약을 취소하면 만약 진료 내역 있으면 삭제
        if (updateStatusReservation.getStatus().equals(Status.Cancelled)){
            medicalChartService.deleteMedicalChart(reservation);
        }
//        진료완료 처리하면 진료 내역 상태도 진료 완료로 업데이트
        else if (updateStatusReservation.getStatus().equals(Status.Completed)){
            medicalChartService.completeMedicalChartByReservation(reservation);
        }
        DoctorResDto doctorResDto = memberFeign.getDoctor(reservation.getDoctorEmail());

//        Redis의 예약 찾기
        String key = reservation.getHospital().getId() + ":"+ reservation.getDoctorEmail();
        RedisDto redisDto = dtoMapper.toRedisDto(reservation);
//        list 에서 해당 예약을 삭제
        redisTemplate.opsForZSet().remove(key, redisDto);
        realTimeService.delete(reservation.getHospital().getName(), doctorResDto.getId().toString(), redisDto.getId().toString());
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void flushDb(){
        redisTemplate.getConnectionFactory().getConnection().flushDb();
        log.info("금일 redis 초기화 완료");
    }

    public List<CheckHospitalListReservationResDto> getDoctorReservation(String doctorEmail, Status status, boolean untact, LocalDate date) {
        List<Reservation> reservations = reservationRepository.findByDoctorEmailAndReservationDateAndUntact(doctorEmail, date, untact);

        // 상태가 null이 아닌 경우 해당 상태로 필터링
        if (status != null) {
            reservations = reservations.stream()
                    .filter(reservation -> reservation.getStatus().equals(status)) // 상태가 일치하는 예약만 필터링
                    .collect(Collectors.toList());
        }

        List<CheckHospitalListReservationResDto> dtos = new ArrayList<>();

        for(Reservation res : reservations){
            ChildResDto childResDto = memberFeign.getMyChild(res.getChildId());
            CheckHospitalListReservationResDto hospitalListReservation = dtoMapper.toHospitalListReservation(res, childResDto.getName(), childResDto.getSsn());
            dtoMapper.setReservationTime(hospitalListReservation, res);
            dtos.add(hospitalListReservation);
        }
        return dtos;
    }

//    예약 총 합 출력
        public Long totalReservationList(){
            Long count = reservationRepository.count();

            return count;
        }
}
