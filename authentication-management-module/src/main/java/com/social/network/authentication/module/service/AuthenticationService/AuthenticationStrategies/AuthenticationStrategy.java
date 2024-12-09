package com.social.network.authentication.module.service.AuthenticationService.AuthenticationStrategies;

import com.social.network.authentication.module.component.exception.AuthenticationTokenException;
import com.social.network.authentication.module.entity.User;

public interface AuthenticationStrategy {
    boolean authenticate(User user, String credentials) throws AuthenticationTokenException;
    String getType();
}
