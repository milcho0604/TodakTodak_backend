package com.padaks.todaktodak.report.domain;

import com.padaks.todaktodak.comment.domain.Comment;
import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Report extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;
//    신고자 이메일
    @Column(nullable = false)
    private String reporterEmail;
//    피신고자 이메일
    @Column(nullable = false)
    private String reportedEmail;
//    이유
    @Column(nullable = false)
    private String reason;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post Pid;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment Cid;
}
