package com.social.network.authentication.module.component.token;

import com.social.network.authentication.module.dto.UserSecurity;
import com.social.network.authentication.module.entity.Role;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public class MultiAuthToken extends AbstractAuthenticationToken {

    @Getter
    private final Long userId;
    @Getter
    private final Collection<Role> role;
    @Getter
    private final String uuidUser;
    @Getter
    private boolean auth;
    @Getter
    private String sessionToken;

    public MultiAuthToken(Long userId,
                          Collection<Role> role,
                          String uuidUser) {
        super(new ArrayList<>());
        this.userId = userId;
        this.role = role;
        this.uuidUser = uuidUser;
        this.auth =true;
    }

    public MultiAuthToken(UserSecurity userSecurity) {
        super(new ArrayList<>());
        this.userId = userSecurity.getUserId();
        this.role = userSecurity.getRoles();
        this.uuidUser = userSecurity.getUuid();
        this.sessionToken = userSecurity.getSessionToken();
        this.auth = true;
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return userId;
    }

    @Override
    public Object getPrincipal() {
        return uuidUser;
    }

    @Override
    public boolean isAuthenticated() {
        return auth;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        auth = b;
    }

    @Override
    public String getName() {
        return uuidUser;
    }
}
