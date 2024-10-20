package com.padaks.todaktodak.comment.dto;

import com.padaks.todaktodak.comment.domain.Comment;
import com.padaks.todaktodak.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReplyCommentSaveDto {
    private Long postId;
    private Long parentId;
    @NotEmpty(message = "content is essential")
    private String content;

    public Comment toEntity(Post post, Comment parent, String writerEmail, String name, String profileImg){
        return Comment.builder()
                .post(post)
                .doctorEmail(writerEmail)
                .content(this.content)
                .name(name)
                .profileImg(profileImg)
                .parent(parent)
                .build();
    }
}
