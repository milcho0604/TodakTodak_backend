package com.padaks.todaktodak.cs.domain;

import com.padaks.todaktodak.chatroom.domain.ChatRoom;
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
}
