package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.dto.JwtAuthenticationResponseDto;
import ru.t1.java.demo.dto.SignInRequestDto;
import ru.t1.java.demo.dto.SignUpRequestDto;
import ru.t1.java.demo.jwt.AuthenticationService;

/**
 * Контроллер для обработки запросов, связанных с аутентификацией пользователей.
 * Предоставляет эндпоинты для регистрации и входа пользователей.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    /**
     * Обрабатывает запрос на регистрацию нового пользователя.
     *
     * @param request Объект запроса на регистрацию, содержащий данные пользователя.
     * @return Объект ответа, содержащий JWT-токен для аутентифицированного пользователя.
     */
    @PostMapping("/sign-up")
    public JwtAuthenticationResponseDto signUp(@RequestBody SignUpRequestDto request) {
        return authenticationService.signUp(request);
    }

    /**
     * Обрабатывает запрос на вход пользователя в систему.
     *
     * @param request Объект запроса на вход, содержащий данные для аутентификации.
     * @return Объект ответа, содержащий JWT-токен для аутентифицированного пользователя.
     */
    @PostMapping("/sign-in")
    public JwtAuthenticationResponseDto signIn(@RequestBody SignInRequestDto request) {
        return authenticationService.signIn(request);
    }
}