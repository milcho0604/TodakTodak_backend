package com.padaks.todaktodak.reservation.dto;

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
public class TodayReservaitionResDto {
    private String childName;
    private String hospitalName;
    private String doctorName;
    private LocalDate reservationDate;
    private LocalTime reservationTime;

    private 
}
