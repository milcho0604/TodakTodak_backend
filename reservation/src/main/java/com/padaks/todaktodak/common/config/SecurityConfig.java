package com.padaks.todaktodak.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthFilter jwtAuthFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/ws/**").permitAll()
                .antMatchers(
                        "/", "/member/get/**", "/member/doctor/**", "/member/create", "/member/login",
                        "/member/hospital/login", "/member/verification/**", "/member/send-verification-code",
                        "/verify/**", "/member/doctorList", "/member/doctorList/**", "/member/doctors",
                        "/member/untact/**", "/member/find/**", "/member/reset/**", "/member/hospitalName",
                        "/member/success", "/member/get/member", "/member/hospital-admin/register",
                        "/member/hospital-admin/accept", "/member/getInfo/**", "/member/report/count/**",
                        "/doctor/**", "/child/detail/**", "/notification/create", "/review/list/**",
                        "/review/detail/**", "/review/doctor/**", "/review/untact/**", "/review/reserve/**",
                        "/hospital/get/hospitalName/**", "/hospital/get/hospital", "/hospital/get/info/*",
                        "/hospital/sorted/list", "/hospital/good/list", "/hospital/hospital-admin/register",
                        "/hospital/detail/*", "/hospital/list", "/hospital-operating-hours/detail/*",
                        "/hospital-operating-hours/getBreakTime/*", "/reservation/hospital/list",
                        "/review/doctor/detail/*", "/reservation/get/member",
                        "/hospital-operating-hours/getBreakTime/*", "/doctor-operating-hours/*",
                        "/comment/get/**", "/comment/list/**", "/comment/listBydoctorEmail",
                        "/post/list", "/post/good/list", "/detail/**", "/detail/views/**", "/detail/**/likes"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.getWriter().write("접근 권한이 없습니다.");
                })
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

