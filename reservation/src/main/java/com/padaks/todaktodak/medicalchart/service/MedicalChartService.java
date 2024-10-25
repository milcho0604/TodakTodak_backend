package com.padaks.todaktodak.medicalchart.service;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.common.exception.BaseException;
import com.padaks.todaktodak.medicalchart.domain.MedicalChart;
import com.padaks.todaktodak.medicalchart.dto.MedicalChartResDto;
import com.padaks.todaktodak.medicalchart.dto.MedicalChartSaveReqDto;
import com.padaks.todaktodak.medicalchart.repository.MedicalChartRepository;
import com.padaks.todaktodak.payment.service.PaymentService;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.padaks.todaktodak.common.exception.exceptionType.ReservationExceptionType.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MedicalChartService {
    private final MedicalChartRepository medicalChartRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentService paymentService;

    public MedicalChartResDto medicalChartCreate(MedicalChartSaveReqDto dto) {
        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new BaseException(RESERVATION_NOT_FOUND));
        // medical chart가 이미 있으면 새로 생성 안함
        if (reservation.getMedicalChart()!=null) {
            return new MedicalChartResDto().fromEntity(reservation.getMedicalChart());
        }
//        의사 이메일을 통해 의사를 찾아온 뒤, 비대면 진료비를 요청
        int fee;
        if(reservation.getHospital()==null ||reservation.getHospital().getUntactFee()==null) {
            throw new IllegalArgumentException("예약에 병원 값이 없거나 병원에 fee정보가 존재하지 않습니다.");
        } else fee = Math.toIntExact(reservation.getHospital().getUntactFee());
        MedicalChart medicalChart = dto.toEntity(reservation, fee);
        MedicalChart saved = medicalChartRepository.save(medicalChart);

        paymentService.getMediChartId(saved.getId());

        return new MedicalChartResDto().fromEntity(saved);
    }

    public MedicalChartResDto getMedicalChartByReservationId(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BaseException(RESERVATION_NOT_FOUND));
        MedicalChart medicalChart = medicalChartRepository.findByReservationAndDeletedAtIsNull(reservation)
                .orElseThrow(() -> new BaseException(MEDICALCHART_NOT_FOUND));
        return new MedicalChartResDto().fromEntity(medicalChart);
    }

    public MedicalChartResDto completeMedicalChart(Long medicalChartId) {
        MedicalChart medicalChart = medicalChartRepository.findById(medicalChartId)
                .orElseThrow(() -> new BaseException(MEDICALCHART_NOT_FOUND));
        medicalChart.complete();
        return new MedicalChartResDto().fromEntity(medicalChart);
    }

    public MedicalChartResDto completeMedicalChartByReservation(Reservation reservation) {
        Optional<MedicalChart> medicalChart = medicalChartRepository.findByReservationAndDeletedAtIsNull(reservation);
        if(medicalChart.isPresent()) {
            medicalChart.get().complete();
        }
        return new MedicalChartResDto().fromEntity(medicalChart.orElse(null));
    }

    public MedicalChartResDto payMedicalChart(Long medicalChartId) {
        MedicalChart medicalChart = medicalChartRepository.findById(medicalChartId)
                .orElseThrow(() -> new BaseException(MEDICALCHART_NOT_FOUND));
        medicalChart.pay();
        return new MedicalChartResDto().fromEntity(medicalChart);
    }

    public void deleteMedicalChart(Reservation reservation) {
        Optional<MedicalChart> medicalChart = medicalChartRepository.findByReservationAndDeletedAtIsNull(reservation);
        medicalChart.ifPresent(BaseTimeEntity::updateDeleteAt);
    }
}
