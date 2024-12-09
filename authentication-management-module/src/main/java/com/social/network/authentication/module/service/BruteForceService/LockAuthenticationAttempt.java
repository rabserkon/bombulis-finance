package com.social.network.authentication.module.service.BruteForceService;


import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class LockAuthenticationAttempt extends Exception {
    private int leftTimeBeforeUnblocking;
    @Getter
    private final Date unlockTime;
    private Class<?> tokenClass;

    public LockAuthenticationAttempt(String message, Date unlockTime) {
        super(message);
        this.unlockTime = unlockTime;
    }

    public LockAuthenticationAttempt(String message) {
        super(message);
        this.unlockTime = null;
    }

    public LockAuthenticationAttempt(String message, Date unlockTime, Class<?> tokenClass) {
        super(message);
        this.unlockTime = unlockTime;
        this.tokenClass = tokenClass;
    }


    @Override
    public String getMessage() {
        if(tokenClass == null){
            return super.getMessage();
        } else if (TokenGenerationAttempt.class.isAssignableFrom(tokenClass)) {
            return "Try generate code again after " + calculateUnlockTimeSec();
        } else if (AttemptLogin.class.isAssignableFrom(tokenClass)){
             return "Try logging in to your account again after " + calculateUnlockTime();
        } else {
            return super.getMessage();
        }
    }

    private String calculateUnlockTime(){
        Instant currentInstant = Instant.now();
        Instant unlockInstant = unlockTime.toInstant();

        Duration duration = Duration.between(currentInstant, unlockInstant);
        long minutesRemaining = duration.toMinutes();
        return minutesRemaining + " minutes";
    }

    private String calculateUnlockTimeSec(){
        Instant currentInstant = Instant.now();
        Instant unlockInstant = unlockTime.toInstant();

        Duration duration = Duration.between(currentInstant, unlockInstant);
        return duration.toSeconds() + " second";
    }
}
