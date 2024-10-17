package com.padaks.todaktodak.post.dto;

import com.padaks.todaktodak.comment.dto.CommentDetailDto;
import com.padaks.todaktodak.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDetailDto {
    private Long id;
    private String memberEmail;
    private String name;
    private String title;
    private String content;
    private String profileImgUrl;
    private String postImgUrl;
    private Long likeCount;
    private Long viewCount;
    private LocalDateTime createdTimeAt;
    private LocalDateTime updatedTimeAt;

    public static PostDetailDto fromEntity(Post post, Long viewCount, Long likeCount, String name, String profileImgUrl){
        return PostDetailDto.builder()
                .id(post.getId())
                .memberEmail(post.getMemberEmail())
                .name(name)
                .title(post.getTitle())
                .content(post.getContent())
                .postImgUrl(post.getPostImgUrl())
                .profileImgUrl(profileImgUrl)
                .likeCount(likeCount != null ? likeCount : 0)
                .viewCount(viewCount != null ? viewCount : 0)
                .createdTimeAt(post.getCreatedTimeAt())
                .updatedTimeAt(post.getUpdatedTimeAt())
                .build();
    }

}

