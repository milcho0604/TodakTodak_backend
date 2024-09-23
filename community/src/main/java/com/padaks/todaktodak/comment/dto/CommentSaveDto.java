package com.padaks.todaktodak.comment.dto;

import com.padaks.todaktodak.comment.domain.Comment;
import com.padaks.todaktodak.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentSaveDto {
    private Long id;
    @NotEmpty(message = "postId is essential")
    private Long postId;
    private Long parentId; //대댓글 기능을 위해 추가 : 부모 댓글 ID
    @NotEmpty(message = "doctor Email is essential")
    private String doctorEmail;
    @NotEmpty(message = "content is essential")
    private String content;

    public Comment toEntity(Post post, Comment parent){
        return Comment.builder()
                .post(post)
                .id(this.id)
                .doctorEmail(this.doctorEmail)
                .content(this.content)
                .parent(parent)
                .build();
    }
}
