package com.social.network.authentication.module.service.AuthenticationService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.social.network.authentication.module.dto.UserSecurity;
import com.social.network.authentication.module.component.exception.AuthenticationTokenException;
import com.social.network.authentication.module.component.exception.CodeTokenCreateException;
import com.social.network.authentication.module.component.exception.RedisConnectionException;
import com.social.network.authentication.module.entity.AuthToken;
import com.social.network.authentication.module.entity.Role;
import com.social.network.authentication.module.entity.User;
import com.social.network.authentication.module.event.EnabledConfirmEvent;
import com.social.network.authentication.module.service.NotificationService.NotificationEvent;
import com.social.network.authentication.module.service.NotificationService.NotificationFactory.NotificationServiceType;
import com.social.network.authentication.module.service.NotificationService.NotificationSenderService;
import com.social.network.authentication.module.service.TokenService.ValideData;
import com.social.network.authentication.module.repository.RoleRepository;
import com.social.network.authentication.module.repository.AuthTokenRepository;
import com.social.network.authentication.module.repository.UserRepository;
import com.social.network.authentication.module.service.AuthenticationService.AuthenticationStrategies.AuthenticationStrategyFactory;
import com.social.network.authentication.module.service.BruteForceService.BruteForceProtectionService;
import com.social.network.authentication.module.service.BruteForceService.LockAuthenticationAttempt;
import com.social.network.authentication.module.service.JWTControlService.JWTControlService;
import com.social.network.authentication.module.service.JWTService.JwtService;
import io.jsonwebtoken.JwtException;
import javassist.NotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@PropertySource("classpath:security.properties")
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private UserRepository userRepository;
    private AuthTokenRepository authTokenRepository;
    private JwtService jwtService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private BruteForceProtectionService bruteForceProtectionService;
    private JWTControlService jwtTokenControl;
    private ApplicationEventPublisher eventPublisher;
    private AuthenticationStrategyFactory authenticationStrategyFactory;
    private RoleRepository roleRepository;
    private NotificationSenderService notificationSenderService;

    @Value("${authentication.code.time.valid}")
    private int codeTimeValid;
    @Value("${authentication.code.count.num}")
    private int codeCount;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    @Transactional
    public boolean checkExistsThisUser(String principal, String method) throws CodeTokenCreateException, LockAuthenticationAttempt, NotFoundException {
        final User user = this.findLoginUser(principal);
        if (!user.isAccountNonLocked()){
            throw new NotFoundException("This user is blocked");
        }
        this.bruteForceProtectionService.checkingUserAbilityToAuthenticate(user.getUserId());
        if (method.equals("byCode")){
            this.bruteForceProtectionService.checkingUserAbilityToCodeGenerate(String.valueOf(user.getUserId()));
            AuthToken authToken = this.createCodeToken(user);
            this.sendAuthenticationCodeNotification(authToken.getCode(), user);
        }
        return true;
    }


    @Override
    @Transactional
    public UserSecurity authenticationByJwt(String JWToken, HttpServletRequest request)  {
        try {
            Map<String,Object> userData = this.jwtService.decodeToken(JWToken);
            Long userId = ((Integer) userData.get("id")).longValue();
            boolean sessionControl = jwtTokenControl.validateSession((String) userData.get("sessionToken"), userId, request);
            if (!sessionControl){
                throw new AuthenticationTokenException("jwt not valid");
            }
            return new UserSecurity(
                    userId,
                    (String) userData.get("email"),
                    (Collection<Role>) userData.get("roles"),
                    (String) userData.get("passwordEncrypted"),
                    (String) userData.get("uuid"),
                    true,
                    true,
                    (String) userData.get("sessionToken")
            );
        } catch (JwtException | AuthenticationTokenException e){
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public String authenticationUser(String principal,
                                     String credentials,
                                     String method,
                                     HttpServletRequest request) throws NotFoundException, AuthenticationTokenException, LockAuthenticationAttempt, JsonProcessingException, RedisConnectionException {
        final User user = this.findLoginUser(principal);
        this.bruteForceProtectionService.checkingUserAbilityToAuthenticate(user.getUserId());
        final boolean authentication = authenticationStrategyFactory
                .getStrategy(method).authenticate(user,credentials);
        if (!authentication){
            bruteForceProtectionService.registerLoginFailure(user.getUserId(), request, method);
            throw new NotFoundException("User login or code not correct");
        }
        if (!user.isEnabled()){
            eventPublisher.publishEvent(new EnabledConfirmEvent(user));
            throw new NotFoundException("This user is disabled");
        }
        if (!user.isAccountNonLocked()){
            throw new NotFoundException("This user is blocked");
        }
        List<Role> roles = this.roleRepository.findByUser(user);
        this.bruteForceProtectionService.resetUserAbilityToCodeGenerate(String.valueOf(user.getUserId()));
        this.bruteForceProtectionService.resetBruteForceCounter(user.getUserId(), request, method);
        String sessionToken =  jwtTokenControl.createSession(user.getUserId(), request);
        return this.jwtService.generateTokenToUser(user, roles, sessionToken);
    }

    @Transactional
    public AuthToken createCodeToken(User user) throws CodeTokenCreateException {
            String code = RandomStringUtils.randomNumeric(codeCount);
            final AuthToken authToken = new AuthToken();
            authToken.setAboutToken("LOGIN_BY_CODE");
            authToken.setTokenUUID(UUID.randomUUID().toString());
            authToken.setCreateTime(LocalDateTime.now());
            authToken.setStatus(false);
            authToken.setValidateTime(calculateExpiryDate(codeTimeValid));
            authToken.setCode(bCryptPasswordEncoder.encode(code));
            authToken.setUser(user);
            authToken.setCreateCode(code);
            return this.authTokenRepository.save(authToken);
    }

    public void sendAuthenticationCodeNotification(String code, User user) {
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setUser(user);
        notificationEvent.setSubject("Authentication code");
        notificationEvent.setMessage(String.format("Hello, your code for login is %s !", code));
        notificationSenderService.sendNotification(notificationEvent, NotificationServiceType.EMAIL);
    }

    public static LocalDateTime calculateExpiryDate(int expiryTimeInSec) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.SECOND, expiryTimeInSec);
        return new Date(cal.getTime().getTime())
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private User findLoginUser(String principal) throws NotFoundException {
        logger.debug("Searching for user with principal: " + principal);
        User user = userRepository.findUserByEmailOrLoginAndAccountNonLockedTrueAndEnabledTrue(principal, principal)
                .orElseThrow(() -> {
                    logger.error("User not found for email or login: " + principal);
                    return new NotFoundException("User not found for email or login: " + principal);
                });
        logger.debug("Found user: " + user.getEmail() + " with ID: " + user.getUserId());
        return user;
    }

    public static Date calculateExpiryDate(Date date, int expiryTimeInSec) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, expiryTimeInSec);
        return new Date(cal.getTime().getTime());
    }

    public static <T extends ValideData> boolean isExpire(T time) {
        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(time.getExpireTime());
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setAuthTokenRepository(AuthTokenRepository authTokenRepository) {
        this.authTokenRepository = authTokenRepository;
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    public void setBruteForceProtectionService(BruteForceProtectionService bruteForceProtectionService) {
        this.bruteForceProtectionService = bruteForceProtectionService;
    }

    @Autowired
    public void setJwtTokenControl(JWTControlService jwtTokenControl) {
        this.jwtTokenControl = jwtTokenControl;
    }

    @Autowired
    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Autowired
    public void setAuthenticationStrategyFactory(AuthenticationStrategyFactory authenticationStrategyFactory) {
        this.authenticationStrategyFactory = authenticationStrategyFactory;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setNotificationSenderService(NotificationSenderService notificationSenderService) {
        this.notificationSenderService = notificationSenderService;
    }
}
