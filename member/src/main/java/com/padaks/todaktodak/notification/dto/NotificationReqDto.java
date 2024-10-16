package com.padaks.todaktodak.notification.dto;

import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.notification.domain.FcmNotification;
import com.padaks.todaktodak.notification.domain.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationReqDto {
    private String memberEmail;
    private String title;
    private String content;
    private Type type;
    private Long refId;

    public FcmNotification toEntity(Member member) {
        return FcmNotification.builder()
                .member(member)
                .title(this.title)
                .content(this.content)
                .type(this.type)
                .refId(this.refId)
                .isRead(false)
                .build();
    }
}
