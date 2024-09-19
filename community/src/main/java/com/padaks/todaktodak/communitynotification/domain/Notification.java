package com.padaks.todaktodak.communitynotification.domain;

import com.padaks.todaktodak.comment.domain.Comment;
import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(nullable = false)
    private String memberEmail;
    private String content;
    @ColumnDefault("false")
    private Boolean isRead;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
