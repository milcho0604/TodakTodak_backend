package com.padaks.todaktodak.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.collect.Lists;


@Configuration
public class FcmConfig {

    @Value("${fcm.secret-file:}")
    private String secretFileName;

    @Value("${fcm.secret-file-content:}")
    private String fcmSecretJson;

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccountStream;

            // 로컬 또는 운영 환경에 따라 InputStream 선택
            if (fcmSecretJson != null && !fcmSecretJson.isEmpty()) {
                // 운영 환경: 환경 변수에 주입된 JSON 문자열을 InputStream으로 변환
                serviceAccountStream = new ByteArrayInputStream(fcmSecretJson.getBytes());
            } else if (secretFileName != null && !secretFileName.isEmpty()) {
                // 로컬 환경: JSON 파일을 InputStream으로 로드
                serviceAccountStream = new ClassPathResource(secretFileName).getInputStream();
            } else {
                throw new IllegalStateException("FCM 설정 파일이 없거나, 환경 변수에 JSON 설정이 없습니다.");
            }

            // GoogleCredentials 생성 및 createScoped 추가
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(serviceAccountStream)
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));

            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();

            // FirebaseApp 초기화가 한 번만 이루어지도록 설정
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
            System.out.println("FCM SETTING SUCCESS");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize FCM: " + e.getMessage());
        }
    }
}