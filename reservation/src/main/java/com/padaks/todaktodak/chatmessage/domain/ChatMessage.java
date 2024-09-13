package com.padaks.todaktodak.chatmessage.domain;

import com.padaks.todaktodak.charroom.domain.ChatRoom;
import com.padaks.todaktodak.common.domain.BaseTimeEntity;
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
public class ChatMessage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chating_id")
    private Long id;
    private String senderEmail;
    private String contents;

    @OneToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;
}
