package com.padaks.todaktodak.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DoctorTimeRequestDto {
    private String doctorEmail;
    private LocalDate date;
}
