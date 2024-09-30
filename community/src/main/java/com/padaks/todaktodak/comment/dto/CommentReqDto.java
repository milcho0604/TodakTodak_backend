package com.padaks.todaktodak.comment.dto;

import com.padaks.todaktodak.comment.domain.Comment;
import com.padaks.todaktodak.post.domain.Post;
import com.padaks.todaktodak.report.domain.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReqDto {

    private Long id;
    private String doctorEmail;
    private String content;
    //댓글 대댓글, 대대댓글... 관계
    private Comment parent;
    private LocalDateTime createdTimeAt;
    private LocalDateTime updatedTimeAt;
    private Long PostId;
}
