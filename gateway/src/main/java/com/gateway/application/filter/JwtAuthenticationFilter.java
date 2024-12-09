package com.gateway.application.filter;

import com.gateway.application.service.TokenControlService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config>{

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    public TokenControlService tokenControlService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(JwtAuthenticationFilter.Config config) {
        return (exchange, chain) -> {
            String token = extractToken(exchange);
            if (token == null || !isTokenValid(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            return chain.filter(exchange);
        };
    }


    private String extractToken(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String sessionToken = (String) claims.get("sessionToken");
            Long userId = ((Integer) claims.get("id")).longValue();
            return tokenControlService.validateSession(sessionToken, userId);
        } catch (Exception e) {
            return false;
        }
    }

    public static class Config {
    }
}
