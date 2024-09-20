package com.padaks.todaktodak.reservation.service;

import com.padaks.todaktodak.common.dto.DtoMapper;
import com.padaks.todaktodak.common.exception.BaseException;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.dto.CheckHospitalListReservationReqDto;
import com.padaks.todaktodak.reservation.dto.CheckHospitalListReservationResDto;
import com.padaks.todaktodak.reservation.dto.UpdateStatusReservation;
import com.padaks.todaktodak.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.padaks.todaktodak.common.exception.exceptionType.ReservationExceptionType.RESERVATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationAdminService {

    private final ReservationRepository reservationRepository;
    private final DtoMapper dtoMapper;

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
    }
}
