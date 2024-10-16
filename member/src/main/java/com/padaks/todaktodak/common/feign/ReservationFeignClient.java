package com.padaks.todaktodak.common.feign;

import com.padaks.todaktodak.common.config.FeignConfig;
import com.padaks.todaktodak.common.dto.CheckHospitalListReservationReqDto;
import com.padaks.todaktodak.member.dto.HospitalInfoDto;
import com.padaks.todaktodak.member.dto.ReviewDetailDto;
import com.padaks.todaktodak.member.dto.HospitalFeignDto;
import com.padaks.todaktodak.member.dto.ReservationFeignDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//k8s배포시에는 url = "http://member-service" 추가
@FeignClient(name = "reservation-service",  configuration = FeignConfig.class)
public interface ReservationFeignClient {

    @GetMapping("/hospital/get/hospital")
    HospitalFeignDto getHospitalInfo();

    @GetMapping("/hospital/get/info/{id}")
    HospitalInfoDto getHospitalinfoById(@RequestParam Long id);

    @GetMapping("/reservation/hospital/list")
    ReservationFeignDto getReservation(CheckHospitalListReservationReqDto reqDto);

    //의사별 리뷰 통계
    @GetMapping("/review/doctor/detail/{doctorEmail}")
    ReviewDetailDto getReview(@PathVariable String doctorEmail);

    @GetMapping("/reservation/get/member")
    List<String> getMember(@RequestHeader("Authorization") String token);

}