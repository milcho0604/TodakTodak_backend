package com.padaks.todaktodak.medicalchart.service;

import com.padaks.todaktodak.common.exception.BaseException;
import com.padaks.todaktodak.medicalchart.domain.MedicalChart;
import com.padaks.todaktodak.medicalchart.dto.MedicalChartResDto;
import com.padaks.todaktodak.medicalchart.dto.MedicalChartSaveReqDto;
import com.padaks.todaktodak.medicalchart.repository.MedicalChartRepository;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.padaks.todaktodak.common.exception.exceptionType.ReservationExceptionType.MEDICALCHART_NOT_FOUND;
import static com.padaks.todaktodak.common.exception.exceptionType.ReservationExceptionType.RESERVATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MedicalChartService {
    private final MedicalChartRepository medicalChartRepository;
    private final ReservationRepository reservationRepository;
    public MedicalChartResDto medicalChartCreate(MedicalChartSaveReqDto dto) {
        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new BaseException(RESERVATION_NOT_FOUND));
//        의사 이메일을 통해 의사를 찾아온 뒤, 비대면 진료비를 요청
        String doctorEmail =  reservation.getDoctorEmail();
        // feign 요청 로직
        int fee = 1000;
        MedicalChart medicalChart = dto.toEntity(reservation, fee);
        MedicalChart saved = medicalChartRepository.save(medicalChart);
        return new MedicalChartResDto().fromEntity(saved);
    }

    public MedicalChartResDto getMedicalChartByReservationId(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BaseException(RESERVATION_NOT_FOUND));
        MedicalChart medicalChart = medicalChartRepository.findByReservation(reservation)
                .orElseThrow(() -> new BaseException(MEDICALCHART_NOT_FOUND));
        return new MedicalChartResDto().fromEntity(medicalChart);
    }
}
