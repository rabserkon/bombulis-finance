package com.bombulis.accounting.service.AuthenticationService;

import antlr.Token;
import com.bombulis.accounting.component.MultiAuthToken;
import com.bombulis.accounting.component.exception.AuthenticationJwtException;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    /**
     * Аутентифицирует пользователя по паролю.
     *
     * @param token        Пользователь для аутентификации.
     * @param request
     * @return true, если аутентификация успешна.
     */
    MultiAuthToken authenticationByJwt(String token, HttpServletRequest request) ;

    /**
     * Аутентифицирует пользователя по паролю.
     *
     * @param token       Пользователь для аутентификации.
     * @return true, если аутентификация успешна.
     */
    MultiAuthToken authenticationByJwt(String token);

    String generateLocalToken(String token);
}
