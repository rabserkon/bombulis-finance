package com.bombulis.accounting.service.AuthenticationService;

import com.bombulis.accounting.component.MultiAuthToken;
import com.bombulis.accounting.component.Role;
import com.bombulis.accounting.component.exception.AuthenticationJwtException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.lettuce.core.RedisConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Service
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class AuthenticationServiceImpl implements AuthenticationService, JwtService, JWTControlService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private RedisTemplate redisTemplate;
    private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public MultiAuthToken authenticationByJWT(String JWToken, HttpServletRequest request) throws AuthenticationJwtException {
        try {
            Map<String,Object> userData = this.decodeToken(JWToken);
            Long userId = ((Integer) userData.get("id")).longValue();
            boolean sessionControl = this.validateSession((String) userData.get("sessionToken"), userId, request);
            if (!sessionControl){
                throw new AuthenticationJwtException("jwt not valid");
            }
            return new MultiAuthToken(
                    userId,
                    (Collection<Role>) userData.get("roles"),
                    (String) userData.get("passwordEncrypted"),
                    (String) userData.get("uuid")
            );
        } catch (JwtException | NullPointerException | ClassCastException e){
            throw new AuthenticationJwtException(e.getMessage());
        } catch (RedisConnectionException e){
            throw new AuthenticationJwtException("Service not is not available");
        }
    }

    @Override
    public MultiAuthToken authenticationByJWT(String JWToken) throws AuthenticationJwtException {
        return this.authenticationByJWT(JWToken, null);
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
    public boolean validateSession(String sessionUUID, Long userId, HttpServletRequest request){
       /* try {
            String sessionKey = "session:" + sessionUUID;
            String sessionInfoStr = (String) redisTemplate.opsForValue().get(sessionKey);
            if (sessionInfoStr == null){
                return false;
            }

            SessionInfo sessionInfo = objectMapper.readValue(sessionInfoStr, SessionInfo.class);
            if (sessionInfo != null && sessionInfo.getUserId().equals(userId) && sessionInfo.isActive()) {
                sessionInfo.setUpdateTime(new Date());
                sessionInfo.setDevice(getBrowserData(request));
                redisTemplate.opsForValue().set(sessionKey, objectMapper.writeValueAsString(sessionInfo));
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }*/
        return true;
    }



    public static String getBrowserData(HttpServletRequest request){
        if (request ==  null){
            return "websocket";
        }
        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        return  "Device: " + userAgent.getOperatingSystem().getDeviceType().getName()+ ", " +
                userAgent.getOperatingSystem().getName() + ", " +
                userAgent.getBrowser().getName() + " " +
                userAgent.getBrowserVersion();
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
