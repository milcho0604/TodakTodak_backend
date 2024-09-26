package com.padaks.todaktodak.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationReqDto {
    private String memberEmail;
    private String content;
    private String type;
    private Long refId;
}
