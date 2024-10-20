package com.padaks.todaktodak.payment.dto;

import com.padaks.todaktodak.common.dto.MemberFeignDto;
import com.padaks.todaktodak.hospital.domain.Hospital;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
// 병원관리자 = 병원대표자 = 결제자
public class PaymentMemberResDto {
    private String hospitalName;
    private String memberEmail;
    private String representativeName;
    private String businessRegistrationInfo;
    private String representativePhoneNumber;

    public static PaymentMemberResDto fromEntity(Hospital hospital, MemberFeignDto member) {
        return PaymentMemberResDto.builder()
                .hospitalName(hospital.getName())
                .memberEmail(member.getMemberEmail())
                .representativeName(member.getName())
                .businessRegistrationInfo(hospital.getBusinessRegistrationInfo())
                .representativePhoneNumber(member.getPhoneNumber())
                .build();
    }
}
