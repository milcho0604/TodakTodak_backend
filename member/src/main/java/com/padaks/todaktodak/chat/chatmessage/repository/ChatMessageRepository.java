package com.padaks.todaktodak.chat.chatmessage.repository;

import com.padaks.todaktodak.chat.chatmessage.domain.ChatMessage;
import com.padaks.todaktodak.chat.chatroom.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 채팅방 기준으로 모든 메시지 찾아오기
    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);
}
