package com.bombulis.accounting.component;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class  MultiAuthToken implements Authentication {

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
        return userId.toString();
    }

}
