package com.padaks.todaktodak.common.config;

import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final MemberRepository memberRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        log.info("preSend 진입");

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String bearerToken = accessor.getFirstNativeHeader("token");

            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                bearerToken = bearerToken.substring(7); // "Bearer " 제거
                if (jwtTokenProvider.validateToken(bearerToken)) {
                    String email = jwtTokenProvider.getEmailFromToken(bearerToken);

                    // UserDetails 로드
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    if (userDetails != null) {
                        // Member 검증
                        Member member = memberRepository.findByMemberEmail(email).orElse(null);
                        if (member != null && member.getDeletedAt() == null) {
                            // SecurityContext에 인증 정보 설정
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(userDetails, bearerToken, userDetails.getAuthorities());
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            accessor.setUser(authentication);

                            log.info("WebSocket 연결 성공 - Email: {}", email);
                        } else {
                            log.error("회원 정보가 유효하지 않거나 삭제된 계정입니다.");
                        }
                    } else {
                        log.error("사용자 정보를 로드할 수 없습니다.");
                    }
                } else {
                    log.error("유효하지 않은 JWT 토큰입니다.");
                }
            } else {
                log.error("JWT 토큰이 없습니다.");
            }
        }

        if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            log.info("WebSocket DISCONNECT");
            SecurityContextHolder.clearContext();
        }

        return message;
    }
}
