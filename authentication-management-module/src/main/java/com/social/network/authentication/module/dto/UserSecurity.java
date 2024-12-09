package com.social.network.authentication.module.dto;


import com.social.network.authentication.module.entity.Role;
import lombok.Getter;

import java.util.Collection;

public class UserSecurity {
    @Getter
    private final Long userId;
    @Getter
    private final String email;
    @Getter
    private final Collection<Role> roles;
    @Getter
    private final String passwordEncrypted;
    @Getter
    private final String uuid;
    @Getter
    private final boolean enabled;
    @Getter
    private final boolean accountNonLocked;
    @Getter
    private final String sessionToken;

    public UserSecurity(Long userId,
                        String email,
                        Collection<Role> roles,
                        String passwordEncrypted,
                        String uuid,
                        boolean enabled,
                        boolean accountNonLocked,
                        String sessionToken) {
        this.userId = userId;
        this.email = email;
        this.roles = roles;
        this.passwordEncrypted = passwordEncrypted;
        this.uuid = uuid;
        this.enabled = enabled;
        this.accountNonLocked = accountNonLocked;
        this.sessionToken = JwtSessionDTO.getChatHash(sessionToken);
    }



}
