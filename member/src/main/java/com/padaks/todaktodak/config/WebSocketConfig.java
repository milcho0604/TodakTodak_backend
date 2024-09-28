package com.padaks.todaktodak.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Stomp를 사용하기 위해 MessageBroker 선언
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // CS채팅 웹소켓 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub"); // 구독할 경로 설정
        config.setApplicationDestinationPrefixes("/pub"); // 메시지 전송 경로 접두사 (클라이언트가 메시지를 보내는 경로)
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat") // STOMP 엔드포인트를 통해 클라이언트가 연결함 (ws://localhost:8080/ws/chat)
                .setAllowedOrigins("*");
//                .withSockJS();  //SockJs fallback 지원
    }
}
