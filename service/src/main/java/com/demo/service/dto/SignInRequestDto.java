package com.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для запроса на вход пользователя в систему.
 * Содержит данные, необходимые для аутентификации пользователя.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignInRequestDto {

    /**
     * Имя пользователя (логин).
     */
    private String username;

    /**
     * Пароль пользователя.
     */
    private String password;
}