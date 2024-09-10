package com.padaks.todaktodak.post.domain;

import com.padaks.todaktodak.comment.domain.Comment;
import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.report.domain.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
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
    @Column(nullable = false)
    private String content;
    private String postImgUrl;
    @ColumnDefault("0")
    private int likeCount;

    @OneToMany(mappedBy = "Pid", cascade = CascadeType.ALL)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "Pid", cascade = CascadeType.ALL)
    private List<Report> reportList;

}
