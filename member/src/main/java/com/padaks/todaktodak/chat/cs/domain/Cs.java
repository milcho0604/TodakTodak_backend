package com.padaks.todaktodak.chat.cs.domain;

import com.padaks.todaktodak.chat.chatroom.domain.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Cs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cs_id")
    private Long id;

    private String csContents;

    @Enumerated(EnumType.STRING)
    private CsStatus csStatus;

    @OneToOne(mappedBy = "cs")
    private ChatRoom chatRoom;

    public enum CsStatus {
        INPROCESS, // 처리중
        COMPLETED // 처리완료
    }
}
