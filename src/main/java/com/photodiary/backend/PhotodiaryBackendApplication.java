package com.photodiary.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PhotodiaryBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotodiaryBackendApplication.class, args);
    }

}
