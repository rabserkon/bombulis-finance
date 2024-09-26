package com.bombulis.accounting.component.filter;

import com.bombulis.accounting.component.DefaultBearerTokenResolver;
import com.bombulis.accounting.service.AuthenticationService.AuthenticationService;
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

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private AuthenticationService authenticationService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
    private AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private AuthenticationFailureHandler authenticationFailureHandler = new AuthenticationEntryPointFailureHandler((request, response, exception) -> {
        this.authenticationEntryPoint.commence(request, response, exception);
    });

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException {
        String token;
        try {
            token = this.bearerTokenResolver.resolve(request);
        } catch (BadCredentialsException var10) {
            this.authenticationEntryPoint.commence(request, response, var10);
            return;
        }
        if (token == null) {
            this.logger.trace("Did not process request since did not find bearer token");
            filterChain.doFilter(request, response);
        } else {
            try {
                Authentication authenticationResult = authenticationService.authenticationByJwt(token);
                SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
                context.setAuthentication(authenticationResult);
                SecurityContextHolder.getContext().setAuthentication(authenticationResult);
                filterChain.doFilter(request, response);
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
