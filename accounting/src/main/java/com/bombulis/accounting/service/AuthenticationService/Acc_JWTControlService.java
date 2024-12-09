package com.bombulis.accounting.service.AuthenticationService;

import javax.servlet.http.HttpServletRequest;

public interface Acc_JWTControlService {
    boolean validateSession(String sessionUUID, Long userId, HttpServletRequest request);
}
