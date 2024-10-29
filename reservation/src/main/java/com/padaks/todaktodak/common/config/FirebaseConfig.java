package com.padaks.todaktodak.common.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void init() {
        if (FirebaseApp.getApps().isEmpty()) { // FirebaseApp이 초기화되지 않은 경우에만 초기화
            try {
                ClassLoader classLoader = getClass().getClassLoader();
                InputStream serviceAccount = classLoader.getResourceAsStream("todak-1f8d0-firebase-adminsdk-tbqa8-b7c41789c9.json");

                if (serviceAccount == null) {
                    throw new IllegalArgumentException("Firebase service account file not found");
                }

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl("https://todak-1f8d0-default-rtdb.asia-southeast1.firebasedatabase.app")
                        .build();
                FirebaseApp.initializeApp(options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
