package com.padaks.todaktodak.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthFilter jwtAuthFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()  // CORS 설정 적용
                .and()
                .csrf().disable()  // CSRF 보호 비활성화
                .authorizeRequests()
//                .antMatchers("/**").permitAll()  // 모든 경로에 대해 인증 필요 없음
                .antMatchers("/admin/**").hasRole("ADMIN") // 관리자만 접근 가능
                .antMatchers("/doctor/**").hasAnyRole("DOCTOR", "HOSPTIALADMIN","ADMIN") // 의사만 접근 가능
                .antMatchers("/hospital/**").hasAnyRole("HOSPTIALADMIN", "ADMIN") // 병원 관리자만 접근 가능
                .antMatchers("/", "/all/**").permitAll() // 공개 경로는 모두 접근 가능
                .anyRequest().authenticated() // 그 외 요청은 인증 필요
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
