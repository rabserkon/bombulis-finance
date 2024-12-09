package com.social.network.authentication.module.service.JWTControlService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.network.authentication.module.component.exception.RedisConnectionException;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RedisJWTControlService implements JWTControlService{

    private RedisTemplate redisTemplate;
    private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String createSession(Long userId, HttpServletRequest request) throws JsonProcessingException, RedisConnectionException {
        try {
            String sessionUuid = UUID.randomUUID().toString();
            String sessionKey = "session:" + sessionUuid;
            String userSessionKey = "users_session:" + userId;
            SessionInfo sessionInfo = new SessionInfo(sessionUuid, userId, getBrowserData(request));
            redisTemplate.opsForValue().set(sessionKey, objectMapper.writeValueAsString(sessionInfo));
            redisTemplate.opsForList().leftPush(userSessionKey, sessionUuid);
            return sessionUuid;
        } catch (Exception e){
            throw new RedisConnectionException();
        }

    }

    @Override
    public boolean validateSession(String sessionUUID, Long userId) {
        String sessionKey = "session:" + sessionUUID;
        String sessionInfoStr = (String) redisTemplate.opsForValue().get(sessionKey);
        if (sessionInfoStr == null){
            return false;
        }
        try {
            SessionInfo sessionInfo = objectMapper.readValue(sessionInfoStr, SessionInfo.class);
            if (sessionInfo != null && sessionInfo.getUserId().equals(userId) && sessionInfo.isActive()) {
                return true;
            }
        } catch (JsonProcessingException e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean validateSession(String sessionUUID, Long userId, HttpServletRequest request){
        try {
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
        } catch (JsonProcessingException e) {
            return false;
        }
    }


    @Override
    public void removeSession(Long userId, String sessionUuid) throws JsonProcessingException {
        String sessionKey = "session:" + sessionUuid;
        String userSessionKey = "users_session:" + userId;
        String sessionInfoStr = (String) redisTemplate.opsForValue().get(sessionKey);
        if (sessionInfoStr == null){
            return;
        }
        SessionInfo sessionInfo = objectMapper.readValue(sessionInfoStr, SessionInfo.class);
        if (sessionInfo != null && sessionInfo.getUserId().equals(userId)) {
            if(redisTemplate.delete(sessionKey)){
                redisTemplate.opsForList().remove(userSessionKey, 1, sessionUuid);
            }
        }
    }

    @Override
    public List<SessionInfo> getUserSessions(Long userId) throws RedisConnectionException {
        try {
            String userSessionKey = "users_session:" + userId;
            List<SessionInfo> sessionInfoList = new ArrayList<>();
            List<String> sessions = redisTemplate.opsForList().range(userSessionKey, 0, -1);
            for (String session : sessions){
                String sessionKey = "session:" + session;
                try {
                    session = (String) redisTemplate.opsForValue().get(sessionKey);
                    if (session != null){
                        SessionInfo  sessionInfo = objectMapper.readValue(session, SessionInfo.class);
                        if (sessionInfo != null && sessionInfo.getUserId().equals(userId) && sessionInfo.isActive()) {
                            sessionInfoList.add(sessionInfo);
                        }
                    }
                } catch (JsonProcessingException e) {

                }
            }
            return sessionInfoList;
        } catch (Exception e){
            throw new RedisConnectionException();
        }

    }

    @Override
    public List<String> deleteSessionsByUserId(Long userId) throws JsonProcessingException, RedisConnectionException {
        try {
            String userSessionKey = "users_session:" + userId;
            List<String> sessionUuids = redisTemplate.opsForList().range(userSessionKey, 0, -1);

            for (String sessionUuid : sessionUuids) {
                removeSession(userId, sessionUuid);
            }

            return sessionUuids;
        } catch (Exception e){
            throw new RedisConnectionException();
        }
    }

    public static String getBrowserData(HttpServletRequest request){
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
