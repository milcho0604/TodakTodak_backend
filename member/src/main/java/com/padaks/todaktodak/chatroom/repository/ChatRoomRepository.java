package com.padaks.todaktodak.chatroom.repository;

import com.padaks.todaktodak.chatroom.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
