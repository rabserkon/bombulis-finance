package com.bombulis.accounting.service.AuthenticationService;

import com.bombulis.accounting.component.Acc_MultiAuthToken;

import javax.servlet.http.HttpServletRequest;

public interface Acc_AuthenticationService {
    /**
     * Аутентифицирует пользователя по паролю.
     *
     * @param token        Пользователь для аутентификации.
     * @param request
     * @return true, если аутентификация успешна.
     */
    Acc_MultiAuthToken authenticationByJwt(String token, HttpServletRequest request) ;

    /**
     * Аутентифицирует пользователя по паролю.
     *
     * @param token       Пользователь для аутентификации.
     * @return true, если аутентификация успешна.
     */
    Acc_MultiAuthToken authenticationByJwt(String token);

    String generateLocalToken(String token);
}
