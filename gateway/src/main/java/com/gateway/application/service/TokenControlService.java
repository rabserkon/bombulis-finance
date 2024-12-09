package com.gateway.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.lettuce.core.RedisConnectionException;

import java.util.List;

public interface TokenControlService {
    boolean validateSession(String sessionUUID, Long userId);

}
