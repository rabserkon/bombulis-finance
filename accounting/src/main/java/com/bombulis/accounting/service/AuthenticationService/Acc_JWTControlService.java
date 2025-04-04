package com.bombulis.accounting.service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;

public interface Acc_JWTControlService {
    boolean validateSession(String sessionUUID, Long userId, HttpServletRequest request);
}
