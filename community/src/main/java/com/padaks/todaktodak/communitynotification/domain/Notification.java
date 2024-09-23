package com.padaks.todaktodak.communitynotification.domain;

import com.padaks.todaktodak.comment.domain.Comment;
import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import com.padaks.todaktodak.communitynotification.dto.NotificationListDto;
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


    public Notification saveDto() {
        return Notification.builder()
                .id(this.id)
                .content(this.content)
                .memberEmail(this.memberEmail)
                .comment(this.comment)
                .build();
    }

    public NotificationListDto listFromEntity(){
        return NotificationListDto.builder()
                .id(this.id)
                .memberEmail(this.memberEmail)
                .content(this.content)
                .isRead(this.isRead)
                .comment(this.comment)
                .build();
    }
}
