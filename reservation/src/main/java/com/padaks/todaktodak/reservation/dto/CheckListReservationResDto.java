package com.padaks.todaktodak.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CheckListReservationResDto {
    private String email;
    private ResType type;
}
