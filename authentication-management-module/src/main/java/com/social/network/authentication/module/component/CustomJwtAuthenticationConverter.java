package com.social.network.authentication.module.component;

import com.social.network.authentication.module.component.token.MultiAuthToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        return new MultiAuthToken(
                Long.parseLong(jwt.getClaimAsString("id")),
                new ArrayList<>(),
                jwt.getClaimAsString("uuid"));
    }
}
