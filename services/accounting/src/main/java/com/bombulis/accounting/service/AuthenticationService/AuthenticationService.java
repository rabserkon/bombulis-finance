package com.bombulis.accounting.service.AuthenticationService;

import com.bombulis.accounting.component.MultiAuthToken;
import com.bombulis.accounting.component.exception.AuthenticationJwtException;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    /**
     * Аутентифицирует пользователя по паролю.
     *
     * @param JWToken        Пользователь для аутентификации.
     * @param request
     * @return true, если аутентификация успешна.
     */
    MultiAuthToken authenticationByJWT(String JWToken, HttpServletRequest request) throws AuthenticationJwtException;


    /**
     * Аутентифицирует пользователя по паролю.
     *
     * @param JWToken        Пользователь для аутентификации.
     * @return true, если аутентификация успешна.
     */
    MultiAuthToken authenticationByJWT(String JWToken) throws AuthenticationJwtException;
}
