package com.social.network.authentication.module.service.AuthenticationService.AuthenticationStrategies;

import lombok.Getter;
import lombok.Setter;

public enum AuthenticationType {
    PASSWORD ("byPassword"),
    CODE("byCode");

    @Getter @Setter
    private final String method;

    AuthenticationType(String method) {
        this.method = method;
    }
}
