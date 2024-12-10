package com.social.network.authentication.module.component.filter;

import com.social.network.authentication.module.component.token.MultiAuthToken;
import com.social.network.authentication.module.dto.UserSecurity;
import com.social.network.authentication.module.service.AuthenticationService.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private AuthenticationService authenticationService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String AUTHORIZATION_HEADER = "AuthorizationGlobal";

    private DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
    private AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private AuthenticationFailureHandler authenticationFailureHandler = new AuthenticationEntryPointFailureHandler((request, response, exception) -> {
        this.authenticationEntryPoint.commence(request, response, exception);
    });


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        bearerTokenResolver.setBearerTokenHeaderName(AUTHORIZATION_HEADER);
        String token;
        try {
            token = this.bearerTokenResolver.resolve(request);
        } catch (BadCredentialsException var10) {
            this.authenticationEntryPoint.commence(request, response, var10);
            return;
        }
        if (token == null) {
            this.logger.trace("Did not process request since did not find bearer token");
            chain.doFilter(request, response);
        } else {
            try {
                UserSecurity userSecurity = authenticationService.authenticationByJwt(token, request);
                Authentication authenticationResult = new MultiAuthToken(userSecurity);
                SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
                context.setAuthentication(authenticationResult);
                SecurityContextHolder.getContext().setAuthentication(authenticationResult);
                chain.doFilter(request, response);
            } catch (AuthenticationException e) {
                this.securityContextHolderStrategy.clearContext();
                this.logger.trace("Failed to process authentication request", e);
                this.authenticationFailureHandler.onAuthenticationFailure(request, response, e);
            }
        }
    }

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
