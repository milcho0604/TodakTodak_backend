package com.padaks.todaktodak.chat.chatroom.repository;

import com.padaks.todaktodak.chat.chatroom.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    default ChatRoom findByIdOrThrow(Long id){
        return findById(id).orElseThrow(()-> new EntityNotFoundException("id에 해당하는 채팅방이 없습니다."));
    }

    // 특정 회원(Member)의 ID를 기준으로 채팅방 목록 조회
    List<ChatRoom> findByMemberId(Long memberId);

    // 해당 회원이 속한 모든 채팅방을 recentChatTime 기준 내림차순 정렬 후 페이징 처리
    Page<ChatRoom> findByMemberIdOrderByRecentChatTimeDesc(Long memberId, Pageable pageable);

    // 모든 채팅방을 recentChatTime 기준 내림차순으로 페이징 처리하여 조회
    Page<ChatRoom> findAllByOrderByRecentChatTimeDesc(Pageable pageable);

}
