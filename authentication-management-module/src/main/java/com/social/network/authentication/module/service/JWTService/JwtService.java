package com.social.network.authentication.module.service.JWTService;

import com.social.network.authentication.module.entity.Role;
import com.social.network.authentication.module.entity.User;
import io.jsonwebtoken.JwtException;

import java.util.Collection;
import java.util.Map;

/**
 * Интерфейс для сервиса работы с JSON Web Token (JWT).
 */
public interface JwtService {

    /**
     * Декодирует JWT токен и возвращает его содержимое в виде карты ключ-значение.
     *
     * @param token JWT токен для декодирования.
     * @return Карта с данными из декодированного токена.
     */
    Map<String, Object> decodeToken(String token) throws JwtException;

    /**
     * Генерирует JWT токен для пользователя с указанными ролями и сессионным токеном.
     *
     * @param user         Пользователь, для которого генерируется токен.
     * @param roles        Список ролей пользователя.
     * @param sessionToken Сессионный токен для включения в токен.
     * @return Сгенерированный JWT токен.
     */
    String generateTokenToUser(User user, Collection<Role> roles, String sessionToken);
}
