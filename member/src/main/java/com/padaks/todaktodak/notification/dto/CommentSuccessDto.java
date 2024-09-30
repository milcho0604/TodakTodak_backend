package com.padaks.todaktodak.notification.dto;

import com.padaks.todaktodak.notification.domain.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentSuccessDto {
    private String receiverEmail;
    private String title;
    private Type type;
    private Long postId;
}

