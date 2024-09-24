package com.padaks.todaktodak.payment.service;

import com.padaks.todaktodak.common.config.FeignConfig;
import com.padaks.todaktodak.payment.dto.MemberDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

//k8s배포시에는 url = "http://member-service" 추가
@FeignClient(name = "member-service",  configuration = FeignConfig.class)
public interface MemberFeignClient {
    @GetMapping("/member/get/member")
        // member-service에 구현된 경로
    MemberDto getMemberEmail();  // MemberDto 반환
}