package com.padaks.todaktodak.member.dto;

import com.padaks.todaktodak.member.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DoctorUpdateReqdto {
    private String memberEmail;
    private String password;
    private String confirmPassword;
    private MultipartFile profileImage;
    private String bio;
}
