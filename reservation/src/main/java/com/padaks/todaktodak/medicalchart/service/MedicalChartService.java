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
        int fee;
        if(reservation.getHospital()==null ||reservation.getHospital().getUntactFee()==null) {
            throw new IllegalArgumentException("예약에 병원 값이 없거나 병원에 fee정보가 존재하지 않습니다.");
        } else fee = Math.toIntExact(reservation.getHospital().getUntactFee());
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

    public MedicalChartResDto completeMedicalChart(Long medicalChartId) {
        MedicalChart medicalChart = medicalChartRepository.findById(medicalChartId)
                .orElseThrow(() -> new BaseException(MEDICALCHART_NOT_FOUND));
        medicalChart.complete();
        return new MedicalChartResDto().fromEntity(medicalChart);
    }

    public MedicalChartResDto payMedicalChart(Long medicalChartId) {
        MedicalChart medicalChart = medicalChartRepository.findById(medicalChartId)
                .orElseThrow(() -> new BaseException(MEDICALCHART_NOT_FOUND));
        medicalChart.pay();
        return new MedicalChartResDto().fromEntity(medicalChart);
    }
}
