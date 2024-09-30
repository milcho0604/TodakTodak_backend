package com.padaks.todaktodak.reservation.realtime;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WaitingTurnDto {
    String reservationId;
    String turnNumber;
}
