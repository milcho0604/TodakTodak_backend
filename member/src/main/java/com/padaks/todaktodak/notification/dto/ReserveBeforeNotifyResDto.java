package com.padaks.todaktodak.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReserveBeforeNotifyResDto {
    private String doctorName;
    private String reservationTime;
    private String hospitalName;
    private String reservationDate;
    private String message;
    private String memberEmail;
}
