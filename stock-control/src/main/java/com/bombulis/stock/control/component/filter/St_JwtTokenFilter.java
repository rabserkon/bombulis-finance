package com.bombulis.stock.control.component.filter;

import com.bombulis.stock.control.service.AuthenticationService.St_AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class St_JwtTokenFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private St_AuthenticationService authenticationService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
    private AuthenticationEntryPoint authenticationEntryPoint = new BearerTokenAuthenticationEntryPoint();
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    private AuthenticationFailureHandler authenticationFailureHandler = new AuthenticationEntryPointFailureHandler((request, response, exception) -> {
        this.authenticationEntryPoint.commence(request, response, exception);
    });

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token;
        try {
            token = this.bearerTokenResolver.resolve(request);
        } catch (OAuth2AuthenticationException var10) {
            this.logger.trace("Sending to authentication entry point since failed to resolve bearer token", var10);
            this.authenticationEntryPoint.commence(request, response, var10);
            return;
        }
        if (token == null) {
            this.logger.trace("Did not process request since did not find bearer token");
            filterChain.doFilter(request, response);
        } else {
            BearerTokenAuthenticationToken bearerToken = new BearerTokenAuthenticationToken(token);
            try {
                Authentication authenticationResult = authenticationService.authenticationByJwt(bearerToken.getToken(), request);
                SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
                context.setAuthentication(authenticationResult);
                this.securityContextHolderStrategy.setContext(context);
                this.securityContextRepository.saveContext(context, request, response);
                filterChain.doFilter(request, response);
            } catch (AuthenticationException e) {
                this.securityContextHolderStrategy.clearContext();
                this.logger.trace("Failed to process authentication request", e);
                this.authenticationFailureHandler.onAuthenticationFailure(request, response, e);
            }
        }

    }

    @Autowired
    public void setAuthenticationService(St_AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
