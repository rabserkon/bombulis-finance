package com.bombulis.accounting.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.ArrayList;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken>{

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        return new Acc_MultiAuthToken(
                Long.parseLong(jwt.getClaimAsString("id")),
                new ArrayList<>(),
                jwt.getClaimAsString("uuid"));
    }
}
