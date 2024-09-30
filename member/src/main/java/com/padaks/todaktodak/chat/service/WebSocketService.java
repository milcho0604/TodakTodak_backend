package com.padaks.todaktodak.chat.service;

import com.padaks.todaktodak.chat.chatmessage.domain.ChatMessage;
import com.padaks.todaktodak.chat.chatmessage.dto.ChatMessageReqDto;
import com.padaks.todaktodak.chat.chatmessage.repository.ChatMessageRepository;
import com.padaks.todaktodak.chat.chatroom.domain.ChatRoom;
import com.padaks.todaktodak.chat.chatroom.repository.ChatRoomRepository;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class WebSocketService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    public void sendMessage(Long chatRoomId, ChatMessageReqDto dto){
        log.info("DTO: {}", dto);
        // chat room 찾기
        ChatRoom chatRoom = chatRoomRepository.findByIdOrThrow(dto.getChatRoomId());
        log.info("ChatRoom: {}", chatRoom);
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Member Email: {}", memberEmail);
        // 보낸 사람 찾기
        Member sender = memberRepository.findByMemberEmailOrThrow(memberEmail);
        log.info("Sender: {}", sender);
        ChatMessage chatMessage = ChatMessageReqDto.toEntity(chatRoom, sender, dto.getContents());
        chatMessageRepository.save(chatMessage); // 메시지 저장

        messagingTemplate.convertAndSend("/sub/" + chatRoomId, dto);
    }
}
