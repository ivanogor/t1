package ru.t1.java.demo.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.JwtAuthenticationResponseDto;
import ru.t1.java.demo.dto.SignInRequestDto;
import ru.t1.java.demo.dto.SignUpRequestDto;
import ru.t1.java.demo.model.User;
import ru.t1.java.demo.model.enums.Role;
import ru.t1.java.demo.service.impl.security.JwtServiceImpl;
import ru.t1.java.demo.service.security.UserService;

/**
 * Сервис для аутентификации пользователей.
 * Обрабатывает регистрацию и вход пользователей в систему.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрация нового пользователя.
     *
     * @param request Данные пользователя для регистрации.
     * @return Объект ответа, содержащий JWT-токен для аутентифицированного пользователя.
     */
    public JwtAuthenticationResponseDto signUp(SignUpRequestDto request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponseDto(jwt);
    }

    /**
     * Аутентификация пользователя.
     *
     * @param request Данные пользователя для аутентификации.
     * @return Объект ответа, содержащий JWT-токен для аутентифицированного пользователя.
     */
    public JwtAuthenticationResponseDto signIn(SignInRequestDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponseDto(jwt);
    }
}