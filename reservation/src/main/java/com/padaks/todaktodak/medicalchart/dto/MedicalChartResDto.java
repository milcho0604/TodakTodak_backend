package com.padaks.todaktodak.medicalchart.dto;

import com.padaks.todaktodak.medicalchart.domain.MedicalChart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MedicalChartResDto {
    private Long id;
    private int fee;
    private String paymentStatus;
    private Long reservationId;

    public MedicalChartResDto fromEntity(MedicalChart medicalChart) {
        this.id = medicalChart.getId();
        this.fee = medicalChart.getFee();
        this.paymentStatus = String.valueOf(medicalChart.getPaymentStatus());
        this.reservationId = medicalChart.getReservation().getId();
        return this;
    }
}
