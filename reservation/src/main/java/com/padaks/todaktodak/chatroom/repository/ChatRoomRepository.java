package com.padaks.todaktodak.chatroom.repository;

import com.padaks.todaktodak.chatroom.domain.UntactChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<UntactChatRoom, Long> {
}
