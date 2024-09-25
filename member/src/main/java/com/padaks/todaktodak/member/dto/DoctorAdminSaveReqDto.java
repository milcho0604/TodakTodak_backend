package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.member.domain.Member;
import com.padaks.todaktodak.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDoctorSaveReqDto {
    @NotEmpty(message = "name is essential")
    private String name;
    @NotEmpty(message = "email is essential")
    private String memberEmail;
    @NotEmpty(message = "password is essential")
    private String password;
    private String phoneNumber;
    private String bio;
//    private String profileImgUrl;
    private Long hospitalId;

    @Builder.Default
    private Role role = Role.Doctor;

    @Builder.Default
    private boolean isVerified = false;

    public Member toEntity(String password, Long hospitalId) {
        return Member.builder()
                .password(password)
                .name(this.name)
                .memberEmail(this.memberEmail)
                .bio(this.bio)
                .role(this.role)
                .hospitalId(hospitalId)
                .build();
    }
}

