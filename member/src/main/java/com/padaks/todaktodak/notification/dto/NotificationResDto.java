package com.padaks.todaktodak.notification.dto;

import com.padaks.todaktodak.notification.domain.Notification;
import com.padaks.todaktodak.notification.domain.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResDto {
    private Long id;
    private String memberEmail;
    private String content;
    private boolean isRead;
    private Type type;
    private Long refId;

    public NotificationResDto fromEntity(Notification save) {
        this.id = save.getId();
        this.memberEmail = save.getMember().getMemberEmail();
        this.content = save.getContent();
        this.isRead = save.isRead();
        this.type = save.getType();
        this.refId = save.getRefId();
        return this;
    }
}
