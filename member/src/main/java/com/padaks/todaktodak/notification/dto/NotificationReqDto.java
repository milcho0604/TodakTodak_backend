package com.padaks.todaktodak.notification.dto;

import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.notification.domain.Notification;
import com.padaks.todaktodak.notification.domain.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationReqDto {
    private String memberEmail;
    private String content;
    private Type type;
    private Long refId;

    public Notification toEntity(Member member) {
        return Notification.builder()
                .member(member)
                .content(this.content)
                .type(this.type)
                .refId(this.refId)
                .isRead(false)
                .build();
    }
}
