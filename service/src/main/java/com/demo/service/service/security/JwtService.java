package com.demo.service.service.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Интерфейс сервиса для работы с JWT-токенами.
 * Предоставляет методы для извлечения имени пользователя из токена, генерации токена и проверки его валидности.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
public interface JwtService {

    /**
     * Извлекает имя пользователя из JWT-токена.
     *
     * @param token JWT-токен.
     * @return Имя пользователя.
     */
    String extractUserName(String token);

    /**
     * Генерирует JWT-токен для заданных данных пользователя.
     *
     * @param userDetails Данные пользователя.
     * @return Сгенерированный JWT-токен.
     */
    String generateToken(UserDetails userDetails);

    /**
     * Проверяет валидность JWT-токена.
     *
     * @param token       JWT-токен.
     * @param userDetails Данные пользователя.
     * @return true, если токен валиден, иначе false.
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}