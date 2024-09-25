package com.padaks.todaktodak.report.service;

import com.padaks.todaktodak.common.config.FeignConfig;
import com.padaks.todaktodak.report.dto.MemberReportDto;
import com.padaks.todaktodak.report.dto.ReportSaveReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

//k8s배포시에는 url = "http://member-service" 추가
@FeignClient(name = "member-service",  configuration = FeignConfig.class)
public interface MemberFeignClient {
    @GetMapping("/member/get/member")
        // member-service에 구현된 경로
    MemberReportDto getMemberEmail(); //ReportSaveReqDto 반환
}