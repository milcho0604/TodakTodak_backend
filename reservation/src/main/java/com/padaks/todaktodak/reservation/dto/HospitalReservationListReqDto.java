package com.padaks.todaktodak.reservation.dto;

import com.padaks.todaktodak.reservation.domain.ReserveType;
import com.padaks.todaktodak.reservation.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HospitalReservationListReqDto {
    private String HospitalName;
    private ReserveType reserveType;
    private Status status;
    private LocalDate date;
}
