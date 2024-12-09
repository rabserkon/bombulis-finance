package com.social.network.authentication.module.service.RegistrationService;

import com.social.network.authentication.module.dto.ResponseRegistrationDTO;
import com.social.network.authentication.module.dto.UserDTO;
import com.social.network.authentication.module.entity.CodeToken;
import com.social.network.authentication.module.entity.User;
import com.social.network.authentication.module.repository.TokenRepository;
import com.social.network.authentication.module.service.CredentialRecoveryService.TokenException;
import com.social.network.authentication.module.service.KafkaProducerService.KafkaProducerService;
import com.social.network.authentication.module.service.KafkaProducerService.event.RegistrationEvent;
import com.social.network.authentication.module.service.NotificationService.NotificationEvent;
import com.social.network.authentication.module.service.NotificationService.NotificationFactory.NotificationServiceType;
import com.social.network.authentication.module.service.NotificationService.NotificationSenderService;
import com.social.network.authentication.module.service.UserService.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@PropertySource("classpath:security.properties")
public class RegistrationServiceImpl implements RegistrationService{

    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private UserService userService;
    private NotificationSenderService notificationSenderService;
    private KafkaProducerService kafkaProducerService;

    @Value("${authentication.code.count.num}")
    private int registrationTokenConfirmCodeCount;

    private static final int REGISTRATION_TOKEN_VALID_SEC = 86400;
    private static final String REGISTRATION_CONFIRM_TOKEN_TYPE = "REGISTRATION_CONFIRM";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    @Transactional
    public User registrationUser(UserDTO userDTO) throws RegistrationException {
        User createUser = registerUser(userDTO);
        return createUser;
    }

    @Override
    public ResponseRegistrationDTO registrationUserAndConfirmToken(UserDTO userDTO) throws RegistrationException {
        User createUser = registerUser(userDTO);
        RegistrationTokenDTO registrationTokenDTO = createRegistrationConfirmToken(createUser);
        sendRegistrationNotification(createUser, registrationTokenDTO);
        sendRegistrationEvent(createUser);
        return new ResponseRegistrationDTO(
                new UserDTO(createUser),
                registrationTokenDTO.getTokenUUID()
        );
    }

    @Override
    @Transactional
    public RegistrationTokenDTO createRegistrationConfirmToken(User user)  {
        String code = RandomStringUtils.randomNumeric(registrationTokenConfirmCodeCount);
        CodeToken token = new CodeToken();
        token.setAboutToken(REGISTRATION_CONFIRM_TOKEN_TYPE);
        token.setUser(user);
        token.setTokenUUID(UUID.randomUUID().toString());
        token.setStatus(false);
        token.setCreateTime(LocalDateTime.now());
        token.setValidateTime(calculateExpiryDate(LocalDateTime.now(), REGISTRATION_TOKEN_VALID_SEC));
        token.setEncryptedCode(passwordEncoder.encode(code));
        logger.info("Code confirm registration: " + code );
        tokenRepository.save(token);
        return new RegistrationTokenDTO(code,token.getTokenUUID());
    }

    @Override
    @Transactional
    public boolean confirmRegistrationTokenByCode(String loginOrEmail, String code) {

        return false;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public boolean confirmRegistrationTokenByToken(String token, String code) throws TokenException {
        CodeToken codeTokenRegistration = tokenRepository.findCodeTokenByAboutTokenAndCreateTimeBeforeAndValidateTimeAfterAndStatusAndTokenUUID(
                "REGISTRATION_CONFIRM",
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                token
                ).orElseThrow(() -> new TokenException("Token not found"));
        if (!passwordEncoder.matches(code, codeTokenRegistration.getEncryptedCode())){
            throw new TokenException("Confirm is not correct");
        }
        codeTokenRegistration.setExecutionDate(LocalDateTime.now());
        codeTokenRegistration.setStatus(true);
        tokenRepository.save(codeTokenRegistration);
        userService.setEnabledUser(codeTokenRegistration.getUser(), true);

        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setUser(codeTokenRegistration.getUser());
        notificationEvent.setSubject("Successful registration");
        notificationEvent.setMessage(String.format("Hello, %s, thank you for confirming your registration!", codeTokenRegistration.getUser().getLogin()));
        notificationSenderService.sendNotification(notificationEvent, NotificationServiceType.EMAIL);
        return true;
    }

    protected User registerUser(UserDTO userDTO) throws RegistrationException {
        User createUser = userService.createAccount(userDTO);
        userService.addRoles(createUser, "user", "people");
        return createUser;
    }

    protected void sendRegistrationNotification(User user, RegistrationTokenDTO registrationTokenDTO) {
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setUser(user);
        notificationEvent.setSubject("Confirm registration");
        notificationEvent.setMessage(
                "token " + registrationTokenDTO.getTokenUUID() +
                        " code " + registrationTokenDTO.getCode()
        );
        notificationSenderService.sendNotification(notificationEvent, NotificationServiceType.EMAIL);
    }

    protected void sendRegistrationEvent(User user) {
        RegistrationEvent registrationEvent = new RegistrationEvent();
        registrationEvent.setUserId(user.getUserId());
        kafkaProducerService.sendRegistrationEvent(registrationEvent);
    }

    private static LocalDateTime calculateExpiryDate(LocalDateTime time, int addTimeInSec) {
        return time.plusSeconds(addTimeInSec);
    }

    @Autowired
    public void setTokenRepository(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setNotificationSenderService(NotificationSenderService notificationSenderService) {
        this.notificationSenderService = notificationSenderService;
    }

    @Autowired
    public void setKafkaProducerService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }
}
