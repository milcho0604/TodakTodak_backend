package com.padaks.todaktodak.chat.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberChatRoomListResDto {
    // 회원입장 CS채팅 리스트

    private Long chatRoomId; // 채팅방 id

    private LocalDateTime ChatRoomCreatedAt; // 채팅방 생성일자

    private LocalDateTime recentChatTime; // 마지막 채팅시간
}
