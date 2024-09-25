package com.padaks.todaktodak.post.service;

import com.padaks.todaktodak.common.config.FeignConfig;
import com.padaks.todaktodak.post.dto.MemberPostDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

//k8s배포시에는 url = "http://member-service" 추가
@FeignClient(name = "post-member-service",  configuration = FeignConfig.class)
public interface MemberPostFeignClient {
    @GetMapping("/member/get/member")
        // member-service에 구현된 경로
    MemberPostDto getMemberEmail(); //ReportSaveReqDto 반환
}