package com.padaks.todaktodak.common.config;

import com.padaks.todaktodak.member.Handler.OAuth2SuccessHandler;
import com.padaks.todaktodak.member.repository.MemberRepository;
import com.padaks.todaktodak.member.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final MemberRepository memberRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // CSRF 보호 비활성화
                .cors().and() // CORS 활성화
                .authorizeRequests()
                .antMatchers("/**").permitAll() // 모든 요청을 허용
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService) // OAuth2 사용자 서비스 설정
                .and()
                .successHandler(oAuth2SuccessHandler) // OAuth2 로그인 성공 핸들러 설정
                .failureUrl("/login?error=true") // 로그인 실패 시 리다이렉션할 URL
                .and()
                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider, userDetailsService, memberRepository),
                        UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가
    }
}
