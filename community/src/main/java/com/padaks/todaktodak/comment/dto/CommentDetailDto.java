package com.padaks.todaktodak.comment.dto;

import com.padaks.todaktodak.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDetailDto {
    private Long id;
    private String name;
    private String doctorEmail;
    private String content;
    private String profileImg;
    private String hospitalName;
    private LocalDateTime createdTimeAt;
    private LocalDateTime updatedTimeAt;
    private Long PostId;
    private Long parentId;
}
