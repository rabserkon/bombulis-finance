package com.bombulis.accounting.component.filter;

import com.bombulis.accounting.component.MultiAuthToken;
import com.bombulis.accounting.component.exception.AuthenticationJwtException;
import com.bombulis.accounting.service.AuthenticationService.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private AuthenticationService authenticationService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String AUTHORIZATION_HEADER = "Authorization";

    public JwtAuthenticationFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = request.getHeader(AUTHORIZATION_HEADER);

        if (token != null && !token.isEmpty()) {
            try {
                MultiAuthToken multiAuthToken = authenticationService.authenticationByJWT(token, request);
                SecurityContextHolder.getContext().setAuthentication(multiAuthToken);
            } catch (AuthenticationJwtException e) {
                logger.debug(e.getMessage());
            }
        }
        chain.doFilter(request, response);
    }

    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
