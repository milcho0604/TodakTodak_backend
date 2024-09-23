//package com.padaks.todaktodak.configs;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.List;
//
//@Component
//@Slf4j
//public class JwtAuthFilter extends GenericFilter {
//
//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String bearerToken = ((HttpServletRequest) request).getHeader("Authorization");
//
//        try {
//            if (bearerToken != null) {
//                if (!bearerToken.startsWith("Bearer ")) {
//                    throw new AuthenticationServiceException("Bearer 형식이 아닙니다.");
//                }
//                String token = bearerToken.substring(7);
//
//                // Secret Key를 URL-safe Base64로 디코딩하여 사용
//                byte[] decodedSecretKey = Base64.getUrlDecoder().decode(secretKey);
//
//                // JWT 검증 및 claims 추출
//                Claims claims = Jwts.parser()
//                        .setSigningKey(decodedSecretKey)  // URL-safe Base64 디코딩된 Secret Key 사용
//                        .parseClaimsJws(token)
//                        .getBody();
//
//                // 계정이 비활성화되거나 삭제된 경우 처리
//                if (claims.get("deletedAt") != null) {
//                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//                    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    httpServletResponse.getWriter().write("해당 계정은 영구 정지되었습니다.");
//                    return;
//                }
//
//                // 권한 설정 및 Authentication 객체 생성
//                List<GrantedAuthority> authorities = new ArrayList<>();
//                authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
//                UserDetails userDetails = new User(claims.getSubject(), "", authorities);
//                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//
//                // SecurityContextHolder에 설정
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//
//            // 다음 필터로 이동
//            chain.doFilter(request, response);
//        } catch (Exception e) {
//            log.error("JWT 처리 중 오류: {}", e.getMessage());
//            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
//            httpServletResponse.setContentType("application/json");
//            httpServletResponse.getWriter().write("토큰 오류");
//        }
//    }
//}
