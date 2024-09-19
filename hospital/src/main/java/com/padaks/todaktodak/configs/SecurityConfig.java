package com.padaks.todaktodak.configs;

import com.padaks.todaktodak.doctor.repository.DoctorRepository;
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
    private final DoctorRepository doctorRepository;

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
                .antMatchers("**").permitAll() // 모든 요청을 허용
                .anyRequest().authenticated() // 나머지 요청은 인증된 사용자만 허용
                .and()
                .addFilterBefore(new JwtAuthFilter(jwtTokenProvider, userDetailsService, doctorRepository),
                        UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가
    }
}
