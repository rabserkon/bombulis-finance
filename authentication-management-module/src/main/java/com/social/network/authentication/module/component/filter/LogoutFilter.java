package com.social.network.authentication.module.component.filter;

import com.social.network.authentication.module.service.JWTControlService.JWTControlService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutFilter extends OncePerRequestFilter {

    private JWTControlService jwtControlService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() != null){


        } else {
            chain.doFilter(request, response);
        }

    }
}
