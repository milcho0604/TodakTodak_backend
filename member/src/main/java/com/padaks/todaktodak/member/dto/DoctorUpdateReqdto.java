package com.padaks.todaktodak.member.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DoctorUpdateReqdto {
    private String name;
    private String memberEmail;
    private String password;
    private String ConfirmPassword;
    private String phoneNumber;
    private String bio;
    private Long hospitalId;
    private MultipartFile profileImage;
}
