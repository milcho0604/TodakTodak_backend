package com.padaks.todaktodak.event.dto;

import com.padaks.todaktodak.event.domain.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventUpdateReqDto {
    private String title;

    private String content;

    private String type;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
