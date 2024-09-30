package com.padaks.todaktodak.chat.chatmessage.dto;

import com.padaks.todaktodak.chat.chatmessage.domain.ChatMessage;
import com.padaks.todaktodak.chat.chatroom.domain.ChatRoom;
import com.padaks.todaktodak.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageReqDto {

    private String memberEmail;

    private Long chatRoomId; // 채팅방 id

//    private String memberEmail; // 회원이메일

    private String contents; // 메시지 내용

    public void updateEmail(String memberEmail){
        this.memberEmail = memberEmail;
    }

    public static ChatMessage toEntity(ChatRoom chatRoom,
                                       Member sender,
                                       String contents
                                       ){
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .contents(contents)
                .build();
    }
}
