package com.padaks.todaktodak.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.padaks.todaktodak.chat.chatmessage.domain.ChatMessage;
import com.padaks.todaktodak.chat.chatmessage.dto.ChatMessageReqDto;
import com.padaks.todaktodak.chat.chatmessage.repository.ChatMessageRepository;
import com.padaks.todaktodak.chat.chatroom.domain.ChatRoom;
import com.padaks.todaktodak.chat.chatroom.repository.ChatRoomRepository;
import com.padaks.todaktodak.common.config.JwtTokenProvider;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Slf4j
@RestController
public class WebSocketController {

//    private final WebSocketService webSocketService;
    private final JwtTokenProvider jwtTokenProvider;
    private final KafkaTemplate<String, Object> chatKafkaTemplate;
    private final MemberRepository memberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public WebSocketController(
//                                WebSocketService webSocketService,
            JwtTokenProvider jwtTokenProvider,
            @Qualifier("chatKafkaTemplate") KafkaTemplate<String, Object> chatKafkaTemplate, MemberRepository memberRepository, ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository) {
//        this.webSocketService = webSocketService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.chatKafkaTemplate = chatKafkaTemplate;
        this.memberRepository = memberRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    // 웹소켓 메시지를 특정 경로로 매핑한다.
    @MessageMapping("/{chatRoomId}") // /pub/1
    public void sendMessage(ChatMessageReqDto chatMessageReqDto,
                            @DestinationVariable(value = "chatRoomId") Long chatRoomId) throws JsonProcessingException {
        log.info("ChatMessageReqDto : {}", chatMessageReqDto);
        String memberEmail = jwtTokenProvider.getEmailFromToken(chatMessageReqDto.getToken());
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않은 회원입니다."));

//        webSocketService.sendMessage(chatRoomId, memberEmail, chatMessageReqDto); // stomp붙일때만

        // ===== KafKa ======
        // 클라이언트 메시지 전송 -> WebSocketController에서 Kafka 프로듀서로 보냄
        // Kafka가 메시지 ChatTopic에 저장하고, ChatKafkaListener가 해당 메시지 수신
        // ChatKafkaListener는 WebSocketService 호출해서 메시지를 웹소켓을 통해 클라이언트로 전달

        // Kafka 토픽에 메시지를 전송
        chatMessageReqDto.setMemberEmail(memberEmail); // 보낸 사람 설정
        chatMessageReqDto.setSenderName(member.getName());
        chatMessageReqDto.setSenderProfileImgUrl(member.getProfileImgUrl());
        chatMessageReqDto.setCreatedAt(LocalDateTime.now());
        chatMessageReqDto.setSenderId(member.getId());
        // stomp 핸들러로 조립을 해주고 보낸디 -> 토큰 까서 webSocket
        // stomp 핸들러 ? token 직접 보내 ? 안 쓰면 이슈 배포환경이라 https
//        조립을 해서 보낸다. 이렇게 해도 시간까지 반쪽짜리
//        토큰을 못 읽는다.
//        Member sender = memberRepository.findByMemberEmail(memberEmail)
        ChatRoom chatRoom = chatRoomRepository.findByIdOrThrow(chatMessageReqDto.getChatRoomId());
        ChatMessage chatMessage = ChatMessageReqDto.toEntity(chatRoom, member, chatMessageReqDto.getContents());
        ChatMessageReqDto messageDto = ChatMessageReqDto.fromEntity(chatMessageReqDto, chatRoom, member);
        log.info("messageDto : {}", messageDto);
        chatMessageRepository.save(chatMessage); // 메시지 저장

        log.info("WebSocketService: Preparing to send message to WebSocket. ChatRoom ID: {}, MemberEmail: {}", chatRoomId, chatMessageReqDto.getMemberEmail());
        chatKafkaTemplate.send("chat-topic", chatMessageReqDto); // 카프카에 메시지 전송

    }


}
