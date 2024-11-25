package com.demo.service.repository;

import com.demo.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью User.
 * Предоставляет методы для поиска, сохранения и удаления пользователей.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по его имени пользователя (логину).
     *
     * @param username Имя пользователя (логин).
     * @return Optional, содержащий найденного пользователя, или пустой, если пользователь не найден.
     */
    Optional<User> findByUsername(String username);

    /**
     * Проверяет, существует ли пользователь с заданным именем пользователя (логином).
     *
     * @param username Имя пользователя (логин).
     * @return true, если пользователь существует, иначе false.
     */
    boolean existsByUsername(String username);

    /**
     * Проверяет, существует ли пользователь с заданным адресом электронной почты.
     *
     * @param email Адрес электронной почты.
     * @return true, если пользователь существует, иначе false.
     */
    boolean existsByEmail(String email);
}