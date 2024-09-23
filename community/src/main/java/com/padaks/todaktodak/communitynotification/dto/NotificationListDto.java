package com.padaks.todaktodak.communitynotification.dto;

import com.padaks.todaktodak.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationListDto {
    private Long id;
    private String memberEmail;
    private String content;
    private Comment comment;
    private Boolean isRead;
}
