package com.social.network.authentication.module.service.AuthenticationService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.social.network.authentication.module.component.exception.AuthenticationTokenException;
import com.social.network.authentication.module.component.exception.CodeTokenCreateException;
import com.social.network.authentication.module.component.exception.RedisConnectionException;
import com.social.network.authentication.module.dto.UserSecurity;
import com.social.network.authentication.module.service.BruteForceService.LockAuthenticationAttempt;
import javassist.NotFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * Интерфейс для сервиса аутентификации пользователей.
 */
public interface AuthenticationService {

    /**
     * Проверяет существование пользователя по указанному идентификатору (principal) и методу.
     *
     * @param principal Идентификатор пользователя (логин или email).
     * @param method    Метод аутентификации (по паролю(byPassword) или по коду(byCode)).
     * @return true, если пользователь с указанным идентификатором существует и может быть аутентифицирован.
     * @throws NotFoundException      Если пользователь не найден.
     * @throws CodeTokenCreateException Если возникли проблемы при создании кода аутентификации.
     */
    boolean checkExistsThisUser(String principal, String method) throws NotFoundException, CodeTokenCreateException, LockAuthenticationAttempt;

    /**
     * Аутентифицирует пользователя по паролю.
     *
     * @param JWToken        Пользователь для аутентификации.
     * @param request
     * @return true, если аутентификация успешна.
     */
    UserSecurity authenticationByJwt(String JWToken, HttpServletRequest request);

    /**
     * Выполняет аутентификацию пользователя на основе предоставленных учетных данных.
     *
     * @param principal Идентификатор пользователя (логин или email).
     * @param credentials Учетные данные (пароль или код).
     * @param method Метод аутентификации (по паролю(byPassword) или по коду(byCode)).
     * @param request Запрос HTTP, используемый для аутентификации.
     * @return Токен аутентификации, если аутентификация успешна.
     * @throws NotFoundException Если пользователь не найден.
     * @throws AuthenticationTokenException Если возникли проблемы с аутентификацией.
     */
    String authenticationUser(String principal,
                              String credentials,
                              String method,
                              HttpServletRequest request) throws NotFoundException, AuthenticationTokenException, LockAuthenticationAttempt, JsonProcessingException, RedisConnectionException;
}
