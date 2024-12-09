package com.social.network.authentication.module.service.CaptchaService;


import java.io.IOException;

/**
 * Сервис капчи.
 */
public interface ICaptchaService {
    /**
     * Проверяет валидность ответа на Google reCAPTCHA.
     *
     * @param response Ответ от пользователя на Google reCAPTCHA.
     * @param ip адрес авторизации пользователя
     * @return `true`, если ответ валиден, иначе `false`.
     */
    void processResponse(final String response, String ip) throws IOException;

    /**
     * Возвращает ключ сайта для Google reCAPTCHA.
     *
     * @return Ключ сайта для Google reCAPTCHA.
     */
    String getReCaptchaSite();

    /**
     * Возвращает секретный ключ для Google reCAPTCHA.
     *
     * @return Секретный ключ для Google reCAPTCHA.
     */
    String getReCaptchaSecret();
}
