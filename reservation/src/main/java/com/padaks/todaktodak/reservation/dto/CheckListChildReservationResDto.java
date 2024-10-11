package com.padaks.todaktodak.reservation.dto;


import com.padaks.todaktodak.reservation.domain.MedicalItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
// child 별 예약 리스트 정보를 담은 dto
public class CheckListChildReservationResDto {
    private Long id;
    private String hospitalName;
    private String doctorName;
    private String memberName;
    private MedicalItem medicalItem;
    private LocalTime reservationTime;
    private LocalDate reservationDate;
    private String hospitalImgUrl;
    private String doctorImgUrl;
}
