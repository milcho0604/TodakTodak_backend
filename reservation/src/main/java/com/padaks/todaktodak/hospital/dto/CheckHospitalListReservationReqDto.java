package com.padaks.todaktodak.hospital.dto;

import com.padaks.todaktodak.reservation.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CheckHospitalListReservationReqDto {
    private String memberEmail;
    private String doctorEmail;
    private Status status;
}
