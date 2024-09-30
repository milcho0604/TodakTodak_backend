package com.padaks.todaktodak.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.padaks.todaktodak.chat.service.WebSocketService;
import com.padaks.todaktodak.chat.chatmessage.dto.ChatMessageReqDto;
import com.padaks.todaktodak.config.JwtTokenProvider;
import com.padaks.todaktodak.config.StompHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
public class WebSocketController {

    private final WebSocketService webSocketService;
    private final JwtTokenProvider jwtTokenProvider;

    // 웹소켓 메시지를 특정 경로로 매핑한다.
    @MessageMapping("/{chatRoomId}") // /pub/1
    public void sendMessage(ChatMessageReqDto chatMessageReqDto,
                            @DestinationVariable(value = "chatRoomId") Long chatRoomId, Principal principal) throws JsonProcessingException {

//        String email = principal.getName();
//        System.out.println("1번 이메일");
//        System.out.println(email);
//        chatMessageReqDto.updateEmail(email);
        log.info("ChatMessageReqDto : {}", chatMessageReqDto);

        webSocketService.sendMessage(chatRoomId, chatMessageReqDto, principal);
    }


}
