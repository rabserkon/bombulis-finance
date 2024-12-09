package com.bombulis.stock.control.service.AuthenticationService;

import io.jsonwebtoken.JwtException;

import java.util.Map;

public interface St_JwtService {
    /**
     * Декодирует JWT токен и возвращает его содержимое в виде карты ключ-значение.
     *
     * @param token JWT токен для декодирования.
     * @return Карта с данными из декодированного токена.
     */
    Map<String, Object> decodeToken(String token) throws JwtException;
}
