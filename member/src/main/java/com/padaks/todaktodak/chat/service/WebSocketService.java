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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class WebSocketService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    public void sendMessage(Long chatRoomId, ChatMessageReqDto dto, Principal principal){
        log.info("DTO: {}", dto);
        // chat room 찾기
        ChatRoom chatRoom = chatRoomRepository.findByIdOrThrow(dto.getChatRoomId());
        log.info("ChatRoom: {}", chatRoom);

        System.out.println("여기까지");
        String dtoEmail = dto.getMemberEmail();
        System.out.println("dto이메일: "+ dtoEmail);

        // 세션에서 사용자 정보를 가져옴
        if (principal == null) {
            throw new IllegalStateException("인증 정보가 존재하지 않습니다.");
        }



        String memberEmail = principal.getName();
        log.info("시큐리티 Member Email: {}", memberEmail);
//        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
//        System.out.println("멤버 이메일은" + memberEmail);
//        log.info("시큐리티 Member Email: {}", memberEmail);
        // 보낸 사람 찾기
        Member sender = memberRepository.findByMemberEmailOrThrow(memberEmail);
        log.info("Sender: {}", sender);
        ChatMessage chatMessage = ChatMessageReqDto.toEntity(chatRoom, sender, dto.getContents());
        chatMessageRepository.save(chatMessage); // 메시지 저장

        messagingTemplate.convertAndSend("/sub/" + chatRoomId, dto);
    }
}
