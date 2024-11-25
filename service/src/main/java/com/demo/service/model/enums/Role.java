package com.demo.service.model.enums;

/**
 * Перечисление, представляющее роли пользователей в системе.
 * Возможные значения: ROLE_ADMIN (администратор), ROLE_USER (пользователь) и ROLE_MODERATOR (модератор).
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
public enum Role {
    /**
     * Роль администратора.
     */
    ROLE_ADMIN,

    /**
     * Роль обычного пользователя.
     */
    ROLE_USER,

    /**
     * Роль модератора.
     */
    ROLE_MODERATOR
}