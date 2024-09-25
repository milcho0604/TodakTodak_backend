package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.member.domain.Address;
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
public class DoctorSaveReqDto {
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

    private MultipartFile profileImage;

    @Builder.Default
    private Role role = Role.Doctor;

    @Builder.Default
    private boolean isVerified = false;

    public Member toEntity(String password, String imageUrl) {
        return Member.builder()
                .password(password)
                .name(this.name)
                .memberEmail(this.memberEmail)
                .bio(this.bio)
                .profileImgUrl(imageUrl)
                .role(this.role)
                .hospitalId(hospitalId)
                .build();
    }
}

