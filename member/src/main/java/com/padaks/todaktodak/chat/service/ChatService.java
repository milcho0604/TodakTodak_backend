package com.padaks.todaktodak.chat.service;

import com.padaks.todaktodak.chat.chatmessage.dto.ChatMessageResDto;
import com.padaks.todaktodak.chat.chatmessage.repository.ChatMessageRepository;
import com.padaks.todaktodak.chat.chatroom.domain.ChatRoom;
import com.padaks.todaktodak.chat.chatroom.repository.ChatRoomRepository;
import com.padaks.todaktodak.chat.cs.repository.CsRepository;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final CsRepository csRepository;

    private static final Long ADMIN_ID = 1L; // Admin 고정 ID

    // 채팅방 생성
    public ChatRoom createChatRoom(String memberEmail){
        Member member = memberRepository.findByMemberEmailOrThrow(memberEmail);

        // 새로운 채팅방 생성, member와 admin의 1:1 채팅방
        ChatRoom chatRoom = ChatRoom.builder()
                .member(member)
                .build();

        return chatRoomRepository.save(chatRoom);
    }

    // 채팅방 모든 메시지 조회
    public List<ChatMessageResDto> getMessages(Long chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findByIdOrThrow(chatRoomId);
        return chatMessageRepository.findByChatRoom(chatRoom)
                .stream()
                .map(ChatMessageResDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 채팅방 삭제
    public void deleteChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findByIdOrThrow(chatRoomId);
        chatRoom.setDeletedTimeAt(LocalDateTime.now()); // 채팅방 deletedAt = 현재시간
    }
}
