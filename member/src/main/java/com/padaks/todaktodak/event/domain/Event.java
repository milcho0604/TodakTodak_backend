package com.padaks.todaktodak.event.domain;

import com.padaks.todaktodak.child.domain.Child;
import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.event.dto.EventDetailResDto;
import com.padaks.todaktodak.event.dto.EventListResDto;
import com.padaks.todaktodak.event.dto.EventUpdateReqDto;
import com.padaks.todaktodak.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false)
    private String title;

//    @Column(nullable = false)
    private String content;

//    @Column(nullable = false)
    private Type type;

//    @Column(nullable = false)
    private LocalDateTime startTime;

//    @Column(nullable = false)
    private LocalDateTime endTime;

    private String memberEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "child_id", nullable = false)
//    private Child child;

    // updateDto
    public void toUpdate(EventUpdateReqDto dto){
        if (dto.getTitle() != null) {
            this.title = dto.getTitle();
        }

        if (dto.getContent() != null) {
            this.content = dto.getContent();
        }

        if (dto.getStartTime() != null) {
            this.startTime = dto.getStartTime();
        }

        if (dto.getEndTime() != null) {
            this.endTime = dto.getEndTime();
        }
    }

    // 리스트
    public EventListResDto listFromEntity(){
        return EventListResDto.builder()
                .title(this.title)
                .content(this.content)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .createdAt(this.getCreatedAt())
                .build();
    }

    // 디테일
    public EventDetailResDto detailFromEntity(){
        return EventDetailResDto.builder()
                .title(this.title)
                .content(this.content)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .createdAt(this.getCreatedAt())
                .build();
    }

    // 고민중..
    public Event updateType(Type type){
        this.type = type;
        return this;
    }

}
