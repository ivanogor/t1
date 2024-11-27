package ru.t1.java.demo.service.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.t1.java.demo.model.User;

/**
 * Интерфейс сервиса для работы с пользователями.
 * Предоставляет методы для создания, поиска и получения текущего пользователя.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
public interface UserService {

    /**
     * Создает нового пользователя.
     *
     * @param user Объект пользователя для создания.
     * @return Созданный пользователь.
     */
    User create(User user);

    /**
     * Находит пользователя по его имени пользователя (логину).
     *
     * @param username Имя пользователя (логин).
     * @return Найденный пользователь.
     */
    User getByUsername(String username);

    /**
     * Возвращает сервис для загрузки пользовательских данных.
     *
     * @return Сервис для загрузки пользовательских данных.
     */
    UserDetailsService userDetailsService();

    /**
     * Возвращает текущего аутентифицированного пользователя.
     *
     * @return Текущий аутентифицированный пользователь.
     */
    User getCurrentUser();
}