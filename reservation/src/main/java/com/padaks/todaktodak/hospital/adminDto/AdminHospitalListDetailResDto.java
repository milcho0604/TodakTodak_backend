package com.padaks.todaktodak.hospital.adminDto;

import com.padaks.todaktodak.hospital.domain.Hospital;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AdminHospitalListDetailResDto {
    private Long id;
    private String name;
    private String address;
    private String dong;
    private String phoneNumber;
    private String businessRegistrationInfo;
    private String representativeName;
    private String adminPhoneNumber;
    private String adminEmail;
    private Boolean isAccept;

    // list
    public static AdminHospitalListDetailResDto listFromEntity(Hospital hospital) {
        return AdminHospitalListDetailResDto.builder()
                .id(hospital.getId())
                .name(hospital.getName())
                .address(hospital.getAddress())
                .dong(hospital.getDong())
                .phoneNumber(hospital.getPhoneNumber())
                .businessRegistrationInfo(hospital.getBusinessRegistrationInfo())
                .representativeName(hospital.getRepresentativeName())
                .adminEmail(hospital.getAdminEmail())
                .isAccept(hospital.getIsAccept())
                .build();  // 마지막에 build() 호출
    }

    // detail
    public static AdminHospitalListDetailResDto detailFromEntity(Hospital hospital) {
        return AdminHospitalListDetailResDto.builder()
                .id(hospital.getId())
                .name(hospital.getName())
                .address(hospital.getAddress())
                .dong(hospital.getDong())
                .phoneNumber(hospital.getPhoneNumber())
                .businessRegistrationInfo(hospital.getBusinessRegistrationInfo())
                .representativeName(hospital.getRepresentativeName())
                .adminPhoneNumber(hospital.getPhoneNumber()) // 추가된 필드
                .adminEmail(hospital.getAdminEmail())
                .isAccept(hospital.getIsAccept())
                .build();
    }
}

