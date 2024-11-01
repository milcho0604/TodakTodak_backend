package com.padaks.todaktodak.chat.chatmessage.dto;

import com.padaks.todaktodak.chat.chatmessage.domain.ChatMessage;
import com.padaks.todaktodak.chat.chatroom.domain.ChatRoom;
import com.padaks.todaktodak.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageReqDto {

    private Long chatRoomId; // 채팅방 id

    private String token; // 멤버 토큰

    private String contents; // 메시지 내용

    private String memberEmail; // 보낸 사람 이메일

//    ------------------------------------------------------
    private String senderName; // 보낸 사람 이름

    private Long senderId; // 보낸사람 id

    private String senderProfileImgUrl; // 보낸사람 프로필 사진 url

    private LocalDateTime createdAt; // 채팅 생성시각

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

    public static ChatMessageReqDto fromEntity(ChatMessageReqDto dto ,ChatRoom chatRoom, Member sender) {
        return ChatMessageReqDto.builder()
                .chatRoomId(chatRoom.getId())
                .contents(dto.getContents())
                .senderName(sender.getName()) // senderName 추가
                .memberEmail(sender.getMemberEmail()) // memberEmail 추가
                .senderId(sender.getId())
                .senderProfileImgUrl(sender.getProfileImgUrl())
                .createdAt(chatRoom.getRecentChatTime())
                .build();
    }
}
