package com.minecraftservers.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinecraftManagerApp {

    private static final Logger logger = LoggerFactory.getLogger(MinecraftManagerApp.class);

    public static void main(String[] args) {
        SpringApplication.run(MinecraftManagerApp.class, args);
        logger.info("Minecraft Kubernetes Manager is running...");
    }
}
