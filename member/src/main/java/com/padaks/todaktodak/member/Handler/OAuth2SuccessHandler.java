package com.padaks.todaktodak.member.Handler;

import com.padaks.todaktodak.config.JwtTokenprovider;
import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenprovider jwtTokenprovider;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String memberEmail = oAuth2User.getAttribute("memberEmail");
        if (memberEmail == null) {
            Map<String, Object> kakaoAccount = oAuth2User.getAttribute("kakao_account");
            if (kakaoAccount != null && kakaoAccount.containsKey("email")) {
                memberEmail = (String) kakaoAccount.get("email");
            }
            if (memberEmail == null) {
                Object naverObject = oAuth2User.getAttributes().get("response");
                Map<String, Object> naverAccount = (Map<String, Object>) naverObject;
                if (naverAccount != null && naverAccount.containsKey("email")) {
                    memberEmail = (String) naverAccount.get("email");
                }
            }
        }

        System.out.println("핸들러입니다!!! 넘오올까요?");
        System.out.println(memberEmail);
        Member member = memberRepository.findByMemberEmail(memberEmail)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카카오 회원입니다."));

        if (member.getDeletedAt() != null){
            throw new IllegalStateException("탈퇴한 회원입니다.");
        }

//        if (member.getReportCount() >= 5) {
//            member = member.deleteEmail(); // 이메일 삭제
//            memberRepository.save(member);

            // del_user 테이블에 이메일 삽입
//            DelUser delUser = DelUser.toEntity(email);
//            delUserRepository.save(delUser);

//            throw new RuntimeException("해당 계정은 비활성화 상태입니다.");
//        }

        Long getMemberId = member.getId();

        String token = jwtTokenprovider.kakaoToken(memberEmail, getMemberId,"MEMBER");
        System.out.println("토큰은!!!!" + token);

        // 리다이렉트 URL 설정 (아래 먼저는 로컬호스트 환경에서, 두번째는 프론트 환경에서)
//        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8082/loginSuccess")
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/")
//        String targetUrl = UriComponentsBuilder.fromUriString("https://www.teenkiri.site/loginSuccess")
                .queryParam("token", token)
                .build().toUriString();

        System.out.println(token);

        // 리다이렉트 수행
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}