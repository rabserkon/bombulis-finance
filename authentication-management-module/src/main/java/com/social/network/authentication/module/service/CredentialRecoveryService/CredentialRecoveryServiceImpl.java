package com.social.network.authentication.module.service.CredentialRecoveryService;

import com.social.network.authentication.module.entity.AccountToken;
import com.social.network.authentication.module.entity.User;
import com.social.network.authentication.module.repository.TokenRepository;
import com.social.network.authentication.module.repository.UserRepository;
import com.social.network.authentication.module.service.NotificationService.NotificationFactory.NotificationServiceType;
import com.social.network.authentication.module.service.NotificationService.NotificationSenderService;
import com.social.network.authentication.module.service.NotificationService.TokenNotificationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CredentialRecoveryServiceImpl implements CredentialRecoveryService{

    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private NotificationSenderService notificationSenderService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${change.password.time.valid}")
    private int changePasswordValidTime;

    private static final String RESET_PASSWORD_TOKEN_TYPE = "RESET_PASSWORD_TOKEN";

    @Override
    public String createChangePasswordToken(String principal) throws UserAccountException {
        User user = userRepository.findUserByEmailAndAccountNonLockedTrue(principal)
                .orElseThrow(() -> new UserAccountException("User not found"));
        AccountToken token = this.createUserToken(user, "RESET_PASSWORD_TOKEN", changePasswordValidTime);
        sendChangePasswordTokenNotification(user, token);
        return token.getTokenUUID();
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean changePasswordByToken(String token, String newPassword) throws TokenException {
        AccountToken changePasswordToken = getChangePasswordToken(token);
        updateToken(changePasswordToken);
        updateUserPassword(changePasswordToken.getUser(), newPassword);
        return true;
    }

    @Override
    public boolean checkTokenExist(String tokenUUID) throws TokenException {
       AccountToken token = tokenRepository
                .findAccountTokenByAboutTokenAndCreateTimeBeforeAndValidateTimeAfterAndStatusFalseAndTokenUUID(
                        RESET_PASSWORD_TOKEN_TYPE,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        tokenUUID
                )
               .orElseThrow(() -> new TokenException("Token not found"));
        return token != null;
    }

    private void sendChangePasswordTokenNotification(User user, AccountToken token) {
        TokenNotificationEvent notificationEvent = new TokenNotificationEvent();
        notificationEvent.setAboutToken(token.getAboutToken());
        notificationEvent.setToken(token.getTokenUUID());
        notificationEvent.setUser(user);
        notificationEvent.setSubject("Recovery password");
        notificationEvent.setMessage("Go to link if you want create new password by token:" + token.getTokenUUID());
        notificationSenderService.sendNotification(notificationEvent, NotificationServiceType.EMAIL);
    }

    protected AccountToken getChangePasswordToken(String token) throws TokenException {
        return tokenRepository.findAccountTokenByAboutTokenAndCreateTimeBeforeAndValidateTimeAfterAndStatusAndTokenUUID(
                        RESET_PASSWORD_TOKEN_TYPE,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        false,
                        token)
                .orElseThrow(() -> new TokenException("Token update not found or not active"));
    }

    protected void updateToken(AccountToken changePasswordToken) {
        changePasswordToken.setStatus(true);
        changePasswordToken.setExecutionDate(LocalDateTime.now());
        tokenRepository.save(changePasswordToken);
    }

    protected void updateUserPassword(User user, String newPassword) {
        user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public AccountToken createUserToken(User user, String aboutTOKEN, int valideTime)   {
        AccountToken accountToken = new AccountToken();
        accountToken.setAboutToken(aboutTOKEN);
        accountToken.setUser(user);
        accountToken.setTokenUUID(UUID.randomUUID().toString());
        accountToken.setStatus(false);
        accountToken.setCreateTime(LocalDateTime.now());
        accountToken.setValidateTime(calculateExpiryDate(LocalDateTime.now(), valideTime));
        return  this.tokenRepository.save(accountToken);
    }

    private static LocalDateTime calculateExpiryDate(LocalDateTime time, int addTimeInSec) {
        return time.plusSeconds(addTimeInSec);
    }
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setTokenRepository(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
    @Autowired
    public void setNotificationSenderService(NotificationSenderService notificationSenderService) {
        this.notificationSenderService = notificationSenderService;
    }
    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
}
