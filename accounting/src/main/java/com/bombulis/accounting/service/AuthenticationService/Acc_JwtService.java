package com.bombulis.accounting.service.AuthenticationService;


import org.springframework.security.oauth2.jwt.JwtException;

import java.util.Map;

public interface Acc_JwtService {
    /**
     * Декодирует JWT токен и возвращает его содержимое в виде карты ключ-значение.
     *
     * @param token JWT токен для декодирования.
     * @return Карта с данными из декодированного токена.
     */
    Map<String, Object> decodeToken(String token) throws JwtException;
}
