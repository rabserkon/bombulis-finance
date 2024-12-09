package com.bombulis.accounting.component;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class Acc_MultiAuthToken implements Authentication {

    @Getter
    private final Long userId;
    @Getter
    private final Collection<Acc_Role> role;
    @Getter
    private final String password;
    @Getter
    private final String uuidUser;
    @Getter
    private boolean auth;

    public Acc_MultiAuthToken(Long userId,
                              Collection<Acc_Role> role,
                              String uuidUser) {
        this.userId = userId;
        this.role = role;
        this.password = "[null]";
        this.uuidUser = uuidUser;
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
        return userId.toString();
    }

}
