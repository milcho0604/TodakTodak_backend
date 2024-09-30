package com.padaks.todaktodak.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker // Stomp를 사용하기 위해 MessageBroker 선언
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    // CS채팅 웹소켓 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub"); // 구독할 경로 설정
        config.setApplicationDestinationPrefixes("/pub"); // 메시지 전송 경로 접두사 (클라이언트가 메시지를 보내는 경로)
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat") // STOMP 엔드포인트를 통해 클라이언트가 연결함 (ws://localhost:8080/ws/chat)
                .setAllowedOriginPatterns("*") // 모든 출처 허용
                .withSockJS();
    }

    // TCP handshake 시 JWT 인증을 위함. 처음 연결될 때 JWT를 이용해서 단 한 번 유효한 유저인가 판단한다.
    // 클라이언트로부터 들어오는 메시지를 처리하는 MessageChannel을 구성하는 역할을 한다.
    // registration.interceptors 메서드를 사용해서 STOMP 메시지 처리를 구성하는 메시지 채널에 custom한 인터셉터를 추가 구성하여
    // 채널의 현재 인터셉터 목록에 추가하는 단계를 거친다.
//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(stompHandler);
//    }

}