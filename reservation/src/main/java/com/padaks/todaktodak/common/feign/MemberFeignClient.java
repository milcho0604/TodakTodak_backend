package com.padaks.todaktodak.common.feign;

import com.padaks.todaktodak.common.config.FeignConfig;
import com.padaks.todaktodak.common.dto.MemberFeignDto;
import com.padaks.todaktodak.hospital.dto.HospitalDTO.HospitalFeignDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//k8s배포시에는 url = "http://member-service" 추가
@FeignClient(name = "member-service",  configuration = FeignConfig.class)
public interface MemberFeignClient {
    @GetMapping("/member/get/member")
        // member-service에 구현된 경로
    MemberFeignDto getMemberEmail();  // MemberPayDto 반환

}