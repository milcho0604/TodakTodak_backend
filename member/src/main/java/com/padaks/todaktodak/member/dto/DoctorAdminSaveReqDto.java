package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAdminSaveReqDto {
    @NotEmpty(message = "name is essential")
    private String name;
    @NotEmpty(message = "email is essential")
    private String memberEmail;

    @Builder.Default
    private Role role = Role.Doctor;

    @Builder.Default
    private boolean isVerified = false;

    public Member toEntity(String password, Long hospitalId) {
        return Member.builder()
                .password(password)
                .name(this.name)
                .memberEmail(this.memberEmail)
                .role(this.role)
                .hospitalId(hospitalId)
                .build();
    }
}

