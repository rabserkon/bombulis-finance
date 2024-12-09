package com.bombulis.stock.control.service.AuthenticationService;

import com.bombulis.stock.control.component.St_MultiAuthToken;
import jakarta.servlet.http.HttpServletRequest;

public interface St_AuthenticationService {
    /**
     * Аутентифицирует пользователя по паролю.
     *
     * @param token        Пользователь для аутентификации.
     * @param request
     * @return true, если аутентификация успешна.
     */
    St_MultiAuthToken authenticationByJwt(String token, HttpServletRequest request) ;

    /**
     * Аутентифицирует пользователя по паролю.
     *
     * @param token       Пользователь для аутентификации.
     * @return true, если аутентификация успешна.
     */
    St_MultiAuthToken authenticationByJwt(String token);

    String generateLocalToken(String token);
}
