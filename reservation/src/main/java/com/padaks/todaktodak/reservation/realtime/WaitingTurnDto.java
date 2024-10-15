package com.padaks.todaktodak.reservation.realtime;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class WaitingTurnDto {
    private String hospitalName;
    private String doctorName;
    private String reservationId;
}
