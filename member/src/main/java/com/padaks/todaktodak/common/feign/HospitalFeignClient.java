package com.padaks.todaktodak.common.feign;

import com.padaks.todaktodak.common.config.FeignConfig;
import com.padaks.todaktodak.member.dto.HospitalFeignDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

//k8s배포시에는 url = "http://member-service" 추가
@FeignClient(name = "reservation-service",  configuration = FeignConfig.class)
public interface HospitalFeignClient {

    @GetMapping("/hospital/get/hospital")
    HospitalFeignDto getHospitalInfo();

}