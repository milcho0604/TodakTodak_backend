package com.padaks.todaktodak.reservation.dto;

import com.padaks.todaktodak.reservation.domain.MedicalItem;
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
public class TodayReservationResDto {
    private String childName;
    private String hospitalName;
    private String doctorName;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private Status status;
    private String ssn;
    private ReserveType reservationType;
    private MedicalItem medicalItem;
    private String field;
    private String message;
    private String profileImgUrl;
}
