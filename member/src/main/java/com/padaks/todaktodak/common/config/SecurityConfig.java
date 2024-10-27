package com.padaks.todaktodak.common.config;

import com.padaks.todaktodak.member.Handler.OAuth2SuccessHandler;
import com.padaks.todaktodak.member.repository.MemberRepository;
import com.padaks.todaktodak.member.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
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
                // 권한에 따른 접근 제어 설정
                .antMatchers("/ws/**").permitAll()
//                .antMatchers("/**").permitAll()  // 인증 없이 접근 가능한 경로
                .antMatchers("/ws/**").permitAll()
                .antMatchers("/signal").permitAll()
                .antMatchers("/", // MemberController
                        "/member/get/**",
                        "/member/doctor/**",
                        "/member/create",
                        "/member/login",
                        "/member/hospital/login",
                        "/member/verification/**",
                        "/member/send-verification-code",
                        "/verify/**",
                        "/member/doctorList",
                        "/member/doctorList/**",
                        "/member/doctors",
                        "/member/untact/**",
                        "/member/find/**",
                        "/member/reset/**",
                        "/member/hospitalName",
                        "/member/success",
                        "/member/get/member",
                        "/member/hospital-admin/register",
                        "/member/hospital-admin/accept",
                        "/member/getInfo/**",
                        "/member/report/count/**",

// DoctorController
                        "/doctor/**",

// ChildController
                        "/child/detail/**",

// NotificationController
                        "/notification/create",

// ReviewController
                        "/review/list/**",
                        "/review/detail/**",
                        "/review/doctor/**",
                        "/review/untact/**",
                        "/review/reserve/**",

// HospitalController
                        "/hospital/get/hospitalName/**",
                        "/hospital/get/hospital",
                        "/hospital/get/info/*",
                        "/hospital/sorted/list",
                        "/hospital/good/list",
                        "/hospital/hospital-admin/register",
                        "/hospital/detail/*",
                        "/hospital/list",

// HospitalOperatingHoursController
                        "/hospital-operating-hours/detail/*",
                        "/hospital-operating-hours/getBreakTime/*",

// MemberToReservationFeign
                        "/reservation/hospital/list",
                        "/review/doctor/detail/*",
                        "/reservation/get/member",
                        "/hospital-operating-hours/getBreakTime/*",

// DoctorOperatingHoursController
                        "/doctor-operating-hours/*",

// CommentController
                        "/comment/get/**",
                        "/comment/list/**",
                        "/comment/listBydoctorEmail",

// PostController
                        "/post/list",
                        "/post/good/list",
                        "/detail/**",
                        "/detail/views/**",
                        "/detail/**/likes"
                ).permitAll()  // 인증 없이 접근 가능한 경로
                .anyRequest().authenticated()  // 그 외의 경로는 인증 필요
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
