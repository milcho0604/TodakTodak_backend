package com.padaks.todaktodak.doctor.service;

import com.padaks.todaktodak.configs.JwtTokenProvider;
import com.padaks.todaktodak.configs.SecurityConfig;
import com.padaks.todaktodak.doctor.domain.Doctor;
import com.padaks.todaktodak.doctor.dto.DoctorListDto;
import com.padaks.todaktodak.doctor.dto.DoctorLoginDto;
import com.padaks.todaktodak.doctor.dto.DoctorSaveDto;
import com.padaks.todaktodak.doctor.dto.DoctorUpdateDto;
import com.padaks.todaktodak.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Random;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenprovider;
    private final JavaEmailService emailService;
    private final RedisService redisService;


    public Doctor doctorCreate(DoctorSaveDto dto){
        if (doctorRepository.findByDoctorEmail(dto.getDoctorEmail()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        Doctor doctor = doctorRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPassword())));
        return doctor;
    }

    public String loginDoctor(DoctorLoginDto dto){
        Doctor doctor = doctorRepository.findByDoctorEmail(dto.getDoctorEmail()).orElseThrow(()->new RuntimeException("의사정보를 찾을 수 없습니다."));
        if (!passwordEncoder.matches(dto.getPassword(), doctor.getPassword())){
            throw new RuntimeException("잘못된 비밀번호입니다.");
        }
        return jwtTokenprovider.createToken(doctor.getDoctorEmail(), "DOCTOR", doctor.getId());

    }

    public Page<DoctorListDto> doctorList(Pageable pageable){
        Page<Doctor> doctors = doctorRepository.findAll(pageable);
        return doctors.map(a->a.listFromEntity());
    }

    @Transactional
    public void updateDoctor(DoctorUpdateDto dto, MultipartFile imageSsr){
        String doctorEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Doctor doctor = doctorRepository.findByDoctorEmail(doctorEmail).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 의사입니다."));

        MultipartFile image = (imageSsr != null) ? imageSsr : dto.getProfileImgUrl();
        try{
            if (image!=null && !image.isEmpty()){
                String ImagePathFileName = doctor.getId() + "_" + image.getOriginalFilename();
                byte[] ImagePathByte = image.getBytes();
                //S3 Image upload 필요..
//                String s3ImagePath = uploadAwsFileService.UploadAwsFileAndReturnPath(bgImagePathFileName, ImagePathByte);
//                doctor.toUpdate(dto, s3ImagePath);
            }else {
                doctor.toUpdate(dto, doctor.getProfileImgUrl());
            }
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()){
                doctor.updatePassword(passwordEncoder.encode(dto.getPassword()));
            }
        }catch (IOException e){
            throw new RuntimeException("의사 정보 수정에 실패하였습니다.", e);
        }
        doctorRepository.save(doctor);
    }

    public void deleteAccount(String doctorEmail){
        Doctor doctor = doctorRepository.findByDoctorEmail(doctorEmail)
                .orElseThrow(()->new RuntimeException("사용자 정보를 확인할 수 없습니다. "+ doctorEmail));
        doctor.updateDeleteAt();
        doctorRepository.save(doctor);
    }

    // java 라이브러리 메일 서비스
    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(9999 - 1000 + 1) + 1000;
        return String.valueOf(code);
    }
    // java 라이브러리 메일 서비스
    public void sendVerificationEmail(String doctorEmail) {
        String code = generateVerificationCode();
        redisService.saveVerificationCode(doctorEmail, code);
        emailService.sendSimpleMessage(doctorEmail, "이메일 인증 코드", "인증 코드: " + code);
    }
    // java 라이브러리 메일 서비스
    public boolean verifyEmail(String doctorEmail, String code) {
        if (redisService.verifyCode(doctorEmail, code)) {
            return true;
        } else {
            throw new RuntimeException("인증 코드가 유효하지 않습니다.");
        }
    }
}