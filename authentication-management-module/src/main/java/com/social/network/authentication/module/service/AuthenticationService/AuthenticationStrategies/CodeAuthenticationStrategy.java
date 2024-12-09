package com.social.network.authentication.module.service.AuthenticationService.AuthenticationStrategies;

import com.social.network.authentication.module.component.exception.AuthenticationTokenException;
import com.social.network.authentication.module.entity.AuthToken;
import com.social.network.authentication.module.entity.User;
import com.social.network.authentication.module.repository.AuthTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class CodeAuthenticationStrategy implements AuthenticationStrategy{

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean authenticate(User user, String credentials) throws AuthenticationTokenException {
        List<AuthToken> tokens = this.authTokenRepository
                    .findByCreateTimeBeforeAndValidateTimeAfterAndStatusFalseAndUserUserId(LocalDateTime.now(), LocalDateTime.now(), user.getUserId());
        if (tokens == null) {
            return false;
        }
        for (AuthToken token : tokens) {
            if (bCryptPasswordEncoder.matches(credentials, token.getCode())) {
                token.setStatus(true);
                token.setExecutionDate(LocalDateTime.now());
                authTokenRepository.save(token);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getType() {
        return AuthenticationType.CODE.getMethod();
    }
}
