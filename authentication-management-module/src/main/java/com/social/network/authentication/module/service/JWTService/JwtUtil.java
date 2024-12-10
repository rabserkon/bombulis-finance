package com.social.network.authentication.module.service.JWTService;

import com.social.network.authentication.module.entity.Role;
import com.social.network.authentication.module.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class JwtUtil implements JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Map<String, Object> decodeToken(String token) throws JwtException {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims;
        } catch (JwtException ex) {
            throw new JwtException("Invalid or expired token", ex);
        }
    }


    @Override
    public String generateTokenToUser(User user, Collection<Role> roles, String sessionToken) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUserId());
        claims.put("uuid", user.getUuid());
        claims.put("roles", roles.stream().map(i -> {
            return new com.social.network.authentication.module.service.JWTService.Role(i.getRoleName());
        }).collect(Collectors.toList()));
        claims.put("sessionToken", sessionToken);
        return createToken(claims, String.valueOf(user.getUserId()));
    }

}
