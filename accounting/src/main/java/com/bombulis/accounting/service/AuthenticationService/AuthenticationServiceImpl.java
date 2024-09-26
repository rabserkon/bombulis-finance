package com.bombulis.accounting.service.AuthenticationService;

import com.bombulis.accounting.component.MultiAuthToken;
import com.bombulis.accounting.component.Role;
import com.bombulis.accounting.entity.User;
import com.bombulis.accounting.service.UserService.UserService;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:application-${spring.profiles.active}.properties")
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService, JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Autowired
    private UserService userService;

    private final String SERVICE_NAME = "finance-service";


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public MultiAuthToken authenticationByJwt(String token, HttpServletRequest request)  {
        return this.authenticationByJwt(token);
    }

    @Override
    public MultiAuthToken authenticationByJwt(String token)  {
        try {
            Map<String,Object> authData = this.decodeToken(token);
            Long userId = ((Integer) authData.get("id")).longValue();
            Object service = authData.get("service");
            if (service == null) {
                throw new BadCredentialsException("Your token valid, you need generate token on local service...");
            }
            if (!(service.toString()).equals(SERVICE_NAME)){
                throw new BadCredentialsException("Your token valid, you need generate token on local service...");
            }
            return new MultiAuthToken(
                    userId,
                    (List<Role>) convertRoles(authData.get("roles")),
                    (String) authData.get("uuid")
            );
        } catch (JwtException | NullPointerException | ClassCastException e){
            throw new BadCredentialsException("decode token problem");
        }
    }

    @Override
    public String generateLocalToken(String token) {
        try {
            Map<String,Object> globalTokenClaims = this.decodeToken(token);
            Long userId = ((Integer) globalTokenClaims.get("id")).longValue();
            final User user = userService.existUser(userId);
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", userId);
            claims.put("uuid", (String) globalTokenClaims.get("uuid"));
            claims.put("roles", user.getRoles().stream().map(i -> new Role(i.getName())).collect(Collectors.toList()));
            claims.put("sessionToken", (String) globalTokenClaims.get("sessionToken"));
            claims.put("global-token", (String) globalTokenClaims.get("sessionToken"));
            claims.put("service", SERVICE_NAME);
            return createToken(claims, userId.toString());
        } catch (JwtException | ClassCastException e){
            throw new JwtException("decode token problem");
        }
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private List<Role> convertRoles(Object rolesObject) {
        List<Role> roles = new ArrayList<>();
        if (rolesObject instanceof List) {
            for (Object roleObj : (List<?>) rolesObject) {
                if (roleObj instanceof LinkedHashMap) {
                    LinkedHashMap<?, ?> roleMap = (LinkedHashMap<?, ?>) roleObj;
                    Role role = new Role((String) roleMap.get("name"));
                    roles.add(role);
                }
            }
        }
        return roles;
    }

    public Map<String, Object> decodeToken(String token) throws JwtException {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return claims.getBody();
        } catch (JwtException ex) {
            throw new JwtException("Invalid or expired token", ex);
        }
    }
}
