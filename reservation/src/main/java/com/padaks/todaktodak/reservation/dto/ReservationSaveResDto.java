package com.padaks.todaktodak.reservation.dto;

import com.padaks.todaktodak.reservation.domain.MedicalItem;
import com.padaks.todaktodak.reservation.domain.Reservation;
import com.padaks.todaktodak.reservation.domain.ReserveType;
import com.padaks.todaktodak.reservation.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReservationSaveResDto {
    private Long id;
    private String memberEmail;
    private Long childId;
    private Long hospitalId;
    private String doctorEmail;
    private ReserveType reservationType;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private boolean isUntact;
    private MedicalItem medicalItem;
    private Status status;
    private String field;
    private String message;

    public ReservationSaveResDto fromEntity (Reservation reservation) {
        this.id = reservation.getId();
        this.memberEmail = reservation.getMemberEmail();
        this.childId = reservation.getChildId();
        this.hospitalId = reservation.getHospital().getId();
        this.doctorEmail = reservation.getDoctorEmail();
        this.reservationType = reservation.getReservationType();
        this.reservationDate = reservation.getReservationDate();
        this.reservationTime = reservation.getReservationTime();
        this.isUntact = reservation.isUntact();
        this.medicalItem = reservation.getMedicalItem();
        this.status = reservation.getStatus();
        this.field = reservation.getField();
        this.message = reservation.getMessage();
        return this;
    }
}
