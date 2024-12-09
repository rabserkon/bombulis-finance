package com.social.network.authentication.module.service.BruteForceService;

import com.social.network.authentication.module.service.AuthenticationService.AuthenticationServiceImpl;
import eu.bitwalker.useragentutils.UserAgent;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@PropertySource("classpath:security.properties")
public class DefaultBruteForceProtectionService implements BruteForceProtectionService{

    @Value("${authentication.max.failed.attempts}")
    private int maxFailedLogin;

    @Value("${authentication.lock.time}")
    private int secUserAuthLockTime;

    private Map<Long, List<AttemptLogin>> failedLoginAttempt;
    private Map<Long, List<AttemptLogin>> successfulLoginAttempt;
    private Map<Long, BlockInformation> blockUserInformation;
    private Map<String, TokenGenerationAttempt> tokenGenerationAttempts;

    private static final int MAX_ATTEMPTS = 3;
    private static final Duration ATTEMPT_WINDOW = Duration.ofMinutes(3);

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public DefaultBruteForceProtectionService() {
        this.failedLoginAttempt = new ConcurrentHashMap<>();
        this.successfulLoginAttempt = new ConcurrentHashMap<>();
        this.blockUserInformation = new ConcurrentHashMap<>();
        this.tokenGenerationAttempts = new ConcurrentHashMap<>();
    }

    @Override
    public void checkingUserAbilityToCodeGenerate(String userId) throws LockAuthenticationAttempt {
        TokenGenerationAttempt attempt = tokenGenerationAttempts.get(userId);
        if (attempt == null || attempt.getLastAttemptTime().plus(ATTEMPT_WINDOW).isBefore(LocalDateTime.now())) {
            tokenGenerationAttempts.put(userId, new TokenGenerationAttempt(userId));
        }
        attempt = tokenGenerationAttempts.get(userId);
        if (attempt.getAttempts() >= MAX_ATTEMPTS) {
            LocalDateTime lastAttemptTime = attempt.getLastAttemptTime();
            throw new LockAuthenticationAttempt(null, new Date(lastAttemptTime.plus(ATTEMPT_WINDOW)
                    .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                    TokenGenerationAttempt.class);
        }
        attempt.incrementAttempts();
    }

    @Override
    public void resetUserAbilityToCodeGenerate(String userId) throws LockAuthenticationAttempt {
       tokenGenerationAttempts.remove(userId);
    }

    @Override
    public void registerLoginFailure(long userId, HttpServletRequest request, String method) {
        AttemptLogin failedLogin = new AttemptLogin(userId,
                new Date(),
                request.getRemoteAddr(),
                getBrowserData(request),  method);
        List<AttemptLogin> failedUserLoginAttempt = failedLoginAttempt.computeIfAbsent(userId, k -> new ArrayList<>());
        failedUserLoginAttempt.add(failedLogin);
        failedLoginAttempt.put(userId, failedUserLoginAttempt);
    }

    @Override
    public void resetBruteForceCounter(long userId, HttpServletRequest request, String method) {
        failedLoginAttempt.put(userId, new ArrayList<>());
    }

    @Override
    public void lockAccountBruteForceAttack(long userId) throws NotFoundException {

    }

    @Override
    public void checkingUserAbilityToAuthenticate(long userId) throws NotFoundException, LockAuthenticationAttempt {
        List<AttemptLogin> failedUserLoginAttempt = failedLoginAttempt.getOrDefault(userId, new ArrayList<>());
        BlockInformation blockInformation = blockUserInformation.get(userId);
        if (blockInformation != null){
            Date unlockDate = AuthenticationServiceImpl
                    .calculateExpiryDate(blockInformation.getBlockData(),
                    secUserAuthLockTime);
            if (new Date().after(unlockDate)){
                failedLoginAttempt.put(userId, new ArrayList<>());
                blockUserInformation.remove(userId);
                return;
            } else {
                throw new LockAuthenticationAttempt("Try logging in to your account again in ", unlockDate ,AttemptLogin.class);
            }
        }
        if (failedUserLoginAttempt.size() >= maxFailedLogin){
            if (!blockUserInformation.containsKey(userId)){
                blockUserInformation.put(userId, new BlockInformation(userId, secUserAuthLockTime));
            }
            throw new LockAuthenticationAttempt("Your account has been blocked for an " + calculateBlockTime(secUserAuthLockTime));
        }
        if (failedUserLoginAttempt.size() <= maxFailedLogin){
            return;
        }
    }

    public static String getBrowserData(HttpServletRequest request){
        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        return  "Device: " + userAgent.getOperatingSystem().getDeviceType().getName()+ ", " +
                userAgent.getOperatingSystem().getName() + ", " +
                userAgent.getBrowser().getName() + " " +
                userAgent.getBrowserVersion();
    }

    private String calculateBlockTime(int seconds){
        Duration duration = Duration.ofSeconds(seconds);
        long minutes = duration.toMinutes();
        return minutes + " minutes";
    }
}
