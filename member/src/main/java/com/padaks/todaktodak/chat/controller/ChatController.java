package com.padaks.todaktodak.chat.controller;

import com.padaks.todaktodak.chat.chatmessage.dto.ChatMessageReqDto;
import com.padaks.todaktodak.chat.chatmessage.dto.ChatMessageResDto;
import com.padaks.todaktodak.chat.chatroom.domain.ChatRoom;
import com.padaks.todaktodak.chat.service.ChatService;
import com.padaks.todaktodak.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
@RestController
public class ChatController {
    private final ChatService chatService;

    // 채팅방 생성 (회원이 새로운 상담을 시작할 때)
    @PostMapping("/chatroom/create")
    public ResponseEntity<?> createChatRoom() {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        ChatRoom chatRoom = chatService.createChatRoom(memberEmail);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.CREATED, "채팅방 생성 성공", chatRoom.getId()),HttpStatus.CREATED);
    }


    // 채팅방의 모든 메시지 조회
    @GetMapping("/chatroom/{chatRoomId}/messages")
    public ResponseEntity<?> getMessages(@PathVariable Long chatRoomId) {
        List<ChatMessageResDto> messages = chatService.getMessages(chatRoomId);
        return new ResponseEntity<>(new CommonResDto(HttpStatus.OK, "채팅방 메시지 조회 성공", messages), HttpStatus.OK);
    }

}
