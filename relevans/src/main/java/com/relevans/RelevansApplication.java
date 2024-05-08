package com.relevans;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@SpringBootApplication
@EnableScheduling
public class RelevansApplication {
    @Value("${firebase.service.account.path}")
    private String firebaseServiceAccountPath;
    public static void main(String[] args) {
        SpringApplication.run(RelevansApplication.class, args);
    }

    @PostConstruct
    public void initializeFirebaseAdmin() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream(firebaseServiceAccountPath);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }

}
