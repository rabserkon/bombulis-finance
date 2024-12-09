package com.social.network.authentication.module.service.JWTControlService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.social.network.authentication.module.component.exception.RedisConnectionException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface JWTControlService {

    String createSession(Long userId, HttpServletRequest request) throws JsonProcessingException, RedisConnectionException;

    boolean validateSession(String sessionUUID, Long userId);

    boolean validateSession(String sessionUUID, Long userId, HttpServletRequest request);

    void removeSession(Long userId, String sessionUuid) throws JsonProcessingException;

    List<SessionInfo> getUserSessions(Long userId) throws RedisConnectionException;

    List<String> deleteSessionsByUserId(Long userId) throws JsonProcessingException, RedisConnectionException;
}
