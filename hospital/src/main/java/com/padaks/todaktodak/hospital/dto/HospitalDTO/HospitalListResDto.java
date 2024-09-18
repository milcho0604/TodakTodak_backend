package com.padaks.todaktodak.hospital.dto.HospitalDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalListResDto {
    private Long id; // 병원 id

    private Long standby; // 병원 실시간 대기자 수

    private String distance; // 내위치 ~ 병원 직선거리

    private String name; // 병원이름

    private String address; // 병원주소

    private String todayOperatingHours; // 오늘 영업시간
}
