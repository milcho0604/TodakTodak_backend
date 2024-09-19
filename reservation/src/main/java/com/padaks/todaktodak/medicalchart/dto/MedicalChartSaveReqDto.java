package com.padaks.todaktodak.medicalchart.dto;

import com.padaks.todaktodak.medicalchart.domain.MedicalChart;
import com.padaks.todaktodak.reservation.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MedicalChartSaveReqDto {
    private Long reservationId;

    public MedicalChart toEntity(Reservation reservation, int fee) {
        return MedicalChart.builder()
                .reservation(reservation)
                .fee(fee)
                .paymentStatus(MedicalChart.PaymentStatus.결제요청)
                .build();
    }
}
