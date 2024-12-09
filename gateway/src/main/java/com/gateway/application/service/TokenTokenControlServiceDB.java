package com.gateway.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TokenTokenControlServiceDB implements TokenControlService {

    private RedisTemplate redisTemplate;
    private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public boolean validateSession(String sessionUUID, Long userId) {
        String sessionKey = "session:" + sessionUUID;
        String sessionInfoStr = (String) redisTemplate.opsForValue().get(sessionKey);
        if (sessionInfoStr == null) {
            return false;
        }
        try {
            SessionInfo sessionInfo = objectMapper.readValue(sessionInfoStr, SessionInfo.class);
            return sessionInfo != null
                    && sessionInfo.getUserId().equals(userId)
                    && sessionInfo.isActive();
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse session info from Redis for key: {}", sessionKey, e);
            return false;
        }
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
