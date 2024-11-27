package ru.t1.java.demo.service.impl.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.model.User;
import ru.t1.java.demo.repository.UserRepository;
import ru.t1.java.demo.service.security.UserService;

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
        // Проверка на существование пользователя по email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        // Проверка на существование пользователя по username
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
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}