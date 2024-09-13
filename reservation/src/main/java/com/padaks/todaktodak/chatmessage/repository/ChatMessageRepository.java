package com.padaks.todaktodak.chatmessage.repository;

import com.padaks.todaktodak.chatmessage.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
