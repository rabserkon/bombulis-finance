package com.social.network.authentication.module.service.CredentialRecoveryService;

import com.social.network.authentication.module.component.exception.AuthenticationTokenException;
import javassist.NotFoundException;

/**
 * Интерфейс для сервиса восстановления учетных данных.
 */
public interface CredentialRecoveryService {

    /**
     * Создает токен для изменения пароля пользователя.
     *
     * @param principal Идентификатор пользователя (например, email или логин).
     * @return Сгенерированный токен для изменения пароля.
     * @throws NotFoundException        Если пользователь с заданным идентификатором не найден.
     * @throws AuthenticationTokenException Если произошла ошибка при создании токена.
     */
    String createChangePasswordToken(String principal) throws NotFoundException, AuthenticationTokenException, UserAccountException;

    /**
     * Изменяет пароль пользователя по заданному токену.
     *
     * @param token      Токен для изменения пароля.
     * @param newPassword Новый пароль пользователя.
     * @return true, если пароль успешно изменен, иначе false.
     * @throws NotFoundException        Если пользователь с заданным токеном не найден.
     * @throws AuthenticationTokenException Если произошла ошибка при изменении пароля.
     */
    boolean changePasswordByToken(String token, String newPassword) throws TokenException;

    /**
     * Проверяет существование заданного токена для указанного пользователя.
     *
     * @param uuid      Идентификатор пользователя.
     * @return true, если токен существует для данного пользователя, иначе false.
     */
    boolean checkTokenExist(String uuid) throws TokenException;
}
