package com.padaks.todaktodak.common.dto;

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
    private String status;
}
