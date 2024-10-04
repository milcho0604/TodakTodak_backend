package com.padaks.todaktodak.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationSuccessResDto {
    private String adminEmail;
    private String memberName;
    private String doctorName;
    private String hospitalName;
    private String reservationType;
    private String reservationDate;
    private String reservationTime;
    private String medicalItem;
    private String childId;

}
