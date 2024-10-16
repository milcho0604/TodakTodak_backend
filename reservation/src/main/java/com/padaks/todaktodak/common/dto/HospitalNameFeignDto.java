package com.padaks.todaktodak.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HospitalNameFeignDto {
    private Long id;
    private String hospitalName;
}
