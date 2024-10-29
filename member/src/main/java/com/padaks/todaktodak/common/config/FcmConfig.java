package com.padaks.todaktodak.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import com.google.common.collect.Lists;


@Configuration
public class FcmConfig {
    @Value("${fcm.secret-file}")
    private String secretFileName;

    @PostConstruct
    public void initialize(){
        try {
            //firebase sdk 업데이트로 GoogleCredentials 생성하는 방법 변경 => createScoped 추가
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new FileInputStream(secretFileName)) // 파일 시스템 경로로 파일 읽기
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));

            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();

            // FirebaseApp 초기화 한 번만 이루어지도록 수정
            if (FirebaseApp.getApps().isEmpty()){
                FirebaseApp.initializeApp(options);
            }
            System.out.println("FCM SETTING SUCCESS");
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize FCM: " + e.getMessage());
        }
    }
}
