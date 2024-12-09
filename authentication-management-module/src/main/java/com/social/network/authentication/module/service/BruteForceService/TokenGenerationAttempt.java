package com.social.network.authentication.module.service.BruteForceService;

import lombok.ToString;

import java.time.LocalDateTime;

@ToString
public class TokenGenerationAttempt {
    private String userId;
    private int attempts;
    private LocalDateTime lastAttemptTime;

    public TokenGenerationAttempt(String userId) {
        this.userId = userId;
        this.attempts = 0;
        this.lastAttemptTime = LocalDateTime.now();
    }

    public void incrementAttempts() {
        attempts++;
        lastAttemptTime = LocalDateTime.now();
    }

    public void resetAttempts() {
        attempts = 0;
        lastAttemptTime = null;
    }

    public String getUserId() {
        return userId;
    }

    public int getAttempts() {
        return attempts;
    }

    public LocalDateTime getLastAttemptTime() {
        return lastAttemptTime;
    }
}
