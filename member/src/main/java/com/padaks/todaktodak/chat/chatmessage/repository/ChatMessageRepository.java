package com.padaks.todaktodak.chat.chatmessage.repository;

import com.padaks.todaktodak.chat.chatmessage.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
