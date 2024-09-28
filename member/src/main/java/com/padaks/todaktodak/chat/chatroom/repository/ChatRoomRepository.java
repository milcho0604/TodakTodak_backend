package com.padaks.todaktodak.chat.chatroom.repository;

import com.padaks.todaktodak.chat.chatroom.domain.ChatRoom;
import com.padaks.todaktodak.common.exception.BaseException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import static com.padaks.todaktodak.common.exception.exceptionType.ChatExceptionType.CHATROOM_NOT_FOUND;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    default ChatRoom findByIdOrThrow(Long id){
        return findById(id).orElseThrow(()-> new BaseException(CHATROOM_NOT_FOUND));
    }
}
