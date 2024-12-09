package com.bombulis.stock.control.component;

import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class St_MultiAuthToken implements Authentication {

    @Getter
    private final Long userId;
    @Getter
    private final Collection<St_Role> role;
    @Getter
    private final String password;
    @Getter
    private final String uuidUser;
    @Getter
    private boolean auth;

    public St_MultiAuthToken(Long userId,
                             Collection<St_Role> role,
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
