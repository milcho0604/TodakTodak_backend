package com.padaks.todaktodak.chat.cs.domain;

import com.padaks.todaktodak.chat.chatroom.domain.ChatRoom;
import com.padaks.todaktodak.common.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Cs extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cs_id")
    private Long id;

    private String csContents;

    @Enumerated(EnumType.STRING)
    private CsStatus csStatus;

    @OneToOne(mappedBy = "cs")
    private ChatRoom chatRoom;

}
