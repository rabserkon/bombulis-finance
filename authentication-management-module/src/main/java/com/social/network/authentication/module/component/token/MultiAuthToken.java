package com.social.network.authentication.module.component.token;

import com.social.network.authentication.module.dto.UserSecurity;
import com.social.network.authentication.module.entity.Role;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MultiAuthToken implements Authentication {

    @Getter
    private final Long userId;
    @Getter
    private final Collection<Role> role;
    @Getter
    private final String password;
    @Getter
    private final String uuidUser;
    @Getter
    private boolean auth;
    @Getter
    private String sessionToken;

    public MultiAuthToken(Long userId,
                          Collection<Role> role,
                          String password,
                          String uuidUser) {
        this.userId = userId;
        this.role = role;
        this.password = password;
        this.uuidUser = uuidUser;
        this.auth =true;
    }

    public MultiAuthToken(UserSecurity userSecurity) {
        this.userId = userSecurity.getUserId();
        this.role = userSecurity.getRoles();
        this.password = userSecurity.getPasswordEncrypted();
        this.uuidUser = userSecurity.getUuid();
        this.sessionToken = userSecurity.getSessionToken();
        this.auth = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role;
    }

    @Override
    public Object getCredentials() {
        return password;
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
