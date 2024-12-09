package com.social.network.authentication.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.social.network.authentication.module")
public class AuthApp {
    public static void main(String[] args) {
        SpringApplication.run(AuthApp.class, args);
    }
}
