package com.social.network.authentication.module.service.AuthenticationService.AuthenticationStrategies;

import com.social.network.authentication.module.component.exception.AuthenticationTokenException;
import com.social.network.authentication.module.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordAuthenticationStrategy implements AuthenticationStrategy{

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public boolean authenticate(User user, String credentials) throws AuthenticationTokenException {
        return bCryptPasswordEncoder.matches(credentials, user.getPassword());
    }

    @Override
    public String getType() {
        return AuthenticationType.PASSWORD.getMethod();
    }
}
