package com.padaks.todaktodak.post.domain;

import com.padaks.todaktodak.comment.domain.Comment;
import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.post.dto.PostListDto;
import com.padaks.todaktodak.post.dto.PostUpdateReqDto;
import com.padaks.todaktodak.report.domain.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @Column(nullable = false)
    private String memberEmail;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 3000)
    private String content;
    @Column
    private String postImgUrl;
    @Builder.Default
    private Long likeCount = 0L;

    private String profileImgUrl;

    private String memberName;

    private@Builder.Default
    Long viewCount = 0L;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Report> reportList = new ArrayList<>();

    public PostListDto listFromEntity(Long viewCount, Long likeCount){
        return PostListDto.builder()
                .id(this.id)
                .title(this.title)
                .memberEmail(this.memberEmail)
                .content(this.content)
                .likeCount(likeCount != null ? likeCount : 0)
                .viewCount(viewCount != null ? viewCount : 0)
                .postImgUrl(this.postImgUrl != null ? this.postImgUrl : null)
                .postImgUrl(this.postImgUrl)
                .createdTimeAt(this.getCreatedTimeAt())
                .build();
    }

    public void updateImage(String postImgUrl){
        this.postImgUrl = postImgUrl;
    }

    public Post update(PostUpdateReqDto dto){
        this.title = dto.getTitle();
        this.content = dto.getContent();
        return this;
    }


    // 조회수 업데이트 메서드
    public void updateViewCount(Long viewCount) {
        this.viewCount = (viewCount != null) ? viewCount : 0L; // null이면 0L로 처리
    }

    // 좋아요 수 업데이트 메서드
    public void updateLikeCount(Long likeCount) {
        this.likeCount = (likeCount != null) ? likeCount : 0L; // null이면 0L로 처리
    }

}
