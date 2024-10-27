package com.padaks.todaktodak.common.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import org.springframework.security.access.AccessDeniedException;  // Spring Security의 AccessDeniedException을 import

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtAuthFilter extends GenericFilter {

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String bearerToken = httpServletRequest.getHeader("Authorization");

        try {
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);

                // Secret Key를 Base64 URL-safe로 디코딩하여 사용
                byte[] decodedSecretKey = Base64.getUrlDecoder().decode(secretKey);

                // JWT 검증 및 Claims 추출
                Claims claims = Jwts.parser()
                        .setSigningKey(decodedSecretKey)
                        .parseClaimsJws(token)
                        .getBody();

                // JWT 유효성 확인
                if (claims.getExpiration().before(new Date())) {
                    throw new JwtException("토큰이 만료되었습니다.");
                }

                // 권한 설정 (Role 정보로 권한 부여)
                List<GrantedAuthority> authorities = new ArrayList<>();
                System.out.println(authorities);
                String role = claims.get("role", String.class);
                System.out.println("ROLEs"+role);
                if (role != null) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
                if (authorities.isEmpty()) {
                    httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                    httpServletResponse.getWriter().write("접근 권한이 없습니다.");
                    return;
                }
                // 사용자 정보를 기반으로 Authentication 객체 생성
                UserDetails userDetails = new User(claims.getSubject(), "", authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // 다음 필터로 이동
            chain.doFilter(request, response);
        } catch (JwtException e) {
            log.error("JWT 처리 중 오류: {}", e.getMessage());
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.getWriter().write("토큰 유효성 검증 실패: " + e.getMessage());
        } catch (AccessDeniedException e) {  // 추가된 권한 예외 처리
            log.error("권한이 없어 접근이 거부되었습니다: {}", e.getMessage());
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpServletResponse.getWriter().write("접근 권한이 없습니다.");
        } catch (Exception e) {
            log.error("JWT 처리 중 예기치 못한 오류: {}", e.getMessage());
            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            httpServletResponse.getWriter().write("서버 내부 오류");
        }
    }
}
