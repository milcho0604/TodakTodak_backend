package com.padaks.todaktodak.event.dto;

import com.padaks.todaktodak.event.domain.Event;
import com.padaks.todaktodak.event.domain.Type;
import com.padaks.todaktodak.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventCreateReqDto {
    private String title;

    private String content;

    private Type type;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public Event toEntity(Member member){
        return Event.builder()
                .title(this.title)
                .content(this.content)
                .type(this.type)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .member(member)
                .memberEmail(member.getMemberEmail())
                .build();
    }
}
