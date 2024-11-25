package com.demo.service.service.impl.security;

import com.demo.service.model.User;
import com.demo.service.repository.UserRepository;
import com.demo.service.service.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса для работы с пользователями.
 * Предоставляет методы для создания, поиска и получения текущего пользователя.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    /**
     * Создает нового пользователя.
     *
     * @param user Объект пользователя для создания.
     * @return Созданный пользователь.
     * @throws RuntimeException Если пользователь с таким email или username уже существует.
     */
    @Override
    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Пользователь с таким username уже существует");
        }
        return userRepository.save(user);
    }

    /**
     * Находит пользователя по его имени пользователя (логину).
     *
     * @param username Имя пользователя (логин).
     * @return Найденный пользователь.
     * @throws RuntimeException Если пользователь не найден.
     */
    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    /**
     * Возвращает сервис для загрузки пользовательских данных.
     *
     * @return Сервис для загрузки пользовательских данных.
     */
    @Override
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Возвращает текущего аутентифицированного пользователя.
     *
     * @return Текущий аутентифицированный пользователь.
     */
    @Override
    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}