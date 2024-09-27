package com.padaks.todaktodak.hospital.dto.HospitalDTO;

import com.padaks.todaktodak.hospital.domain.Hospital;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospitalFeignDto {
    Long hospitalId;
    String name;
    String phoneNumber;
}
