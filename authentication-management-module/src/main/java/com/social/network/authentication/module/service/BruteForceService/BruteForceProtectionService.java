package com.social.network.authentication.module.service.BruteForceService;

import javassist.NotFoundException;

import javax.servlet.http.HttpServletRequest;

public interface BruteForceProtectionService {

    void checkingUserAbilityToCodeGenerate(String userId) throws LockAuthenticationAttempt;

    void resetUserAbilityToCodeGenerate(String userId) throws LockAuthenticationAttempt;

    /**
     * Регистрирует неудачную попытку входа пользователя с указанным Id.
     *
     * @param userId    Id пользователя, для которого регистрируется неудачная попытка входа.
     * @param request HttpServletRequest для получения информации о запросе.
     * @param method  метод входа который был использован при неудачной
     *                попытке входа byPassword или byCode.
     */
    void registerLoginFailure(long userId, HttpServletRequest request, String method);

    /**
     * Сбрасывает счетчик попыток атаки методом "brute force" для пользователя с указанным Id.
     * Обчно после удачного входа
     *
     * @param userId    Id пользователя, для которого регистрируется неудачная попытка входа.
     * @param request HttpServletRequest для получения информации о запросе.
     * @param method  метод входа который был использован при неудачной
     *                попытке входа byPassword или byCode.
     */
    void resetBruteForceCounter(long userId, HttpServletRequest request, String method);

    /**
     * Блокирует учетную запись пользователя с указанным Id из-за атаки методом "brute force".
     *
     * @param userId    Id пользователя, для которого регистрируется неудачная попытка входа.
     * @throws NotFoundException      если пользователь с указанным UUID не найден.
     */
    void lockAccountBruteForceAttack(long userId) throws NotFoundException;

    /**
     * Проверяет возможность пользователя аутентифицироваться.
     * Если пользователь пытался аутентифицироваться более раз неудачно, вносит его в блок лист на какое-то время,
     * Если время блокировки вышло, удаляет пользователя из блок-листа при запросе метода,
     * так же обнуляет все неудачные попытки аутентификации
     *
     *
     * @param userId Идентификатор пользователя.
     * @throws NotFoundException       Если пользователь с указанным идентификатором не найден.
     * @throws LockAuthenticationAttempt Если у пользователя превышено количество попыток аутентификации
     *                                  и его аккаунт заблокирован на некоторое время (Выдает время на
     *                                  которое заблокирован аккаунт пользователя или
     *                                  сколько осталось до окончания блокировки).
     */
    void checkingUserAbilityToAuthenticate(long userId) throws NotFoundException, LockAuthenticationAttempt;
}
