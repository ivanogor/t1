package com.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для передачи JWT-токена аутентификации.
 * Содержит JWT-токен, который используется для аутентификации пользователя.
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
public class JwtAuthenticationResponseDto {

    /**
     * JWT-токен для аутентификации.
     */
    private String token;
}