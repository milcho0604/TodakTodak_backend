package com.padaks.todaktodak.common.dto;


import com.padaks.todaktodak.member.dto.ReviewListResDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorInfoDto {

    private String memberEmail;

    private String doctorName; //의사 이름

    private Long doctorId; //의사 Id

    private String profileImg; //의사 사진

    private String hospitalName; //병원 이름

    private Long hospitalId; //병원 Id

    // 리뷰 개수
    private long reviewCount;

    //리뷰 평범
    private double reviewPoint;
}

//의사 프로필 사진 이름 병원이름 평점 리뷰 개수 진료 개수 ResDto 만들어서
