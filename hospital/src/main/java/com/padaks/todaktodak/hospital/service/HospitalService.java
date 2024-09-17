package com.padaks.todaktodak.hospital.service;

import com.padaks.todaktodak.util.DistanceCalculator;
import com.padaks.todaktodak.util.S3ClientFileUpload;
import com.padaks.todaktodak.hospital.domain.Hospital;
import com.padaks.todaktodak.common.dto.DtoMapper;
import com.padaks.todaktodak.hospital.dto.HospitalDTO.HospitalDetailResDto;
import com.padaks.todaktodak.hospital.dto.HospitalDTO.HospitalRegisterReqDto;
import com.padaks.todaktodak.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class HospitalService {
    private final HospitalRepository hospitalRepository;
    private final S3ClientFileUpload s3ClientFileUpload;
    private final DtoMapper dtoMapper;
    private final DistanceCalculator distanceCalculator;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 병원등록
    public Hospital hospitalRegister(HospitalRegisterReqDto dto){

        MultipartFile hospitalImage = dto.getHospitalImage();

        // S3에 파일 업로드
        String imageUrl = s3ClientFileUpload.upload(hospitalImage, bucket); // S3Client를 통해 S3에 이미지 업로드

        // Hospital 엔티티 생성 및 저장
        Hospital hospital = Hospital.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .phoneNumber(dto.getPhoneNumber())
                .description(dto.getDescription())
                .notice(dto.getNotice())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .businessRegistrationInfo(dto.getBusinessRegistrationInfo())
                .representativeName(dto.getRepresentativeName())
                .representativePhoneNumber(dto.getRepresentativePhoneNumber())
                .hospitalImageUrl(imageUrl) // 업로드한 이미지의 URL을 저장
                .build();

        return hospitalRepository.save(hospital); // Hospital 저장

    }

    // 병원 detail 조회
    public HospitalDetailResDto getHospitalDetail(Long id, BigDecimal latitude, BigDecimal longitude) {
        Hospital hospital = hospitalRepository.findByIdOrThrow(id);

        // 병원과 사용자 간의 직선 거리 계산
        String distance = DistanceCalculator.calculateDistance(
                hospital.getLatitude(), hospital.getLongitude(), latitude, longitude);

        // TODO : 이후 레디스에서 실시간 대기자수 값 가져오기
        return HospitalDetailResDto.builder()
                .id(hospital.getId())
//                .standby() // 실시간 대기자 수
                .distance(distance)
                .name(hospital.getName())
                .address(hospital.getAddress())
                .phoneNumber(hospital.getPhoneNumber())
                .description(hospital.getDescription())
                .notice(hospital.getNotice())
                .latitude(hospital.getLatitude())
                .longitude(hospital.getLongitude())
                .businessRegistrationInfo(hospital.getBusinessRegistrationInfo())
                .representativeName(hospital.getRepresentativeName())
                .representativePhoneNumber(hospital.getRepresentativePhoneNumber())
                .build();
    }

}
