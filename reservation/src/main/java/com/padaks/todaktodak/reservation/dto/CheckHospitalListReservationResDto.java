package com.padaks.todaktodak.reservation.dto;

import com.padaks.todaktodak.reservation.domain.MedicalItem;
import com.padaks.todaktodak.reservation.domain.ReserveType;
import com.padaks.todaktodak.reservation.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CheckHospitalListReservationResDto {
    private String doctorName;
    private String doctorEmail;
    private String memberName;
    private String memberEmail;
    private Long childId;
    private String childName;
    private String childSsn;
    private ReserveType reserveType;
    private Status status;
    private MedicalItem medicalItem;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
}
