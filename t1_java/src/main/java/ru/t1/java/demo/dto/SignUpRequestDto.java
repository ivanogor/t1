package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для запроса на регистрацию нового пользователя.
 * Содержит данные, необходимые для создания нового пользователя.
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
public class SignUpRequestDto {

    /**
     * Имя пользователя (логин).
     */
    private String username;

    /**
     * Адрес электронной почты пользователя.
     */
    private String email;

    /**
     * Пароль пользователя.
     */
    private String password;
}