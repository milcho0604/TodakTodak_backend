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
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream serviceAccount = classLoader.getResourceAsStream("padak-todak-firebase-adminsdk-d2ths-d5d3744918.json");

            if (serviceAccount == null) {
                throw new IllegalArgumentException("Firebase service account file not found");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://padak-todak-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
