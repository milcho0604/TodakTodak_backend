//package com.padaks.todaktodak.configs;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Component
//public class JwtTokenProvider {
//
//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    @Value("${jwt.expiration}")
//    private long tokenValidityInMilliseconds;
//
//    // userId를 포함한 createToken 메서드
//    public String createToken(String email, String role, Long memberId) {
//        Claims claims = Jwts.claims().setSubject(email);
//        claims.put("role", role);
//        claims.put("memberId", memberId);  // userId를 클레임에 추가
//
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(validity)
//                .signWith(SignatureAlgorithm.HS512, secretKey)
//                .compact();
//    }
//
//    public String kakaoToken(String email, Long memberId, String role) {
//        Claims claims = Jwts.claims().setSubject(email);
//        claims.put("role", role);
//        claims.put("memberId", memberId);  // userId를 클레임에 추가
//
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(validity)
//                .signWith(SignatureAlgorithm.HS512, secretKey)
//                .compact();
//    }
//
//    // 토큰에서 이메일 추출
//    public String getEmailFromToken(String token) {
//        return Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    // 토큰에서 userId 추출
//    public Long getMemberIdFromToken(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token)
//                .getBody();
//        return claims.get("memberId", Long.class);
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}
