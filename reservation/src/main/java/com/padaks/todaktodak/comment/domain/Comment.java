package com.padaks.todaktodak.comment.domain;

import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.post.domain.Post;
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
public class Comment extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    @Column(nullable = false)
    private String memberEmail;
    @Column(nullable = false)
    private String content;
    @ColumnDefault("0")
    private int likeCount;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post Pid;

    @OneToMany(mappedBy = "Cid")
    private List<Comment> commentList;

}
