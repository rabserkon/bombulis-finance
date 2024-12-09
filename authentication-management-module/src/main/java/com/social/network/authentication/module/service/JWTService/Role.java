package com.social.network.authentication.module.service.JWTService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority, Serializable {
    private static final long serialVersionUID = 1L;
    @Getter @Setter
    private long id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private long userId;

    public Role(String name, long userId) {
        this.name = name;
        this.userId = userId;
    }

    public Role(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
