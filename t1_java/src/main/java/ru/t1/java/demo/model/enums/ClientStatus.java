package ru.t1.java.demo.model.enums;

/**
 * Перечисление, представляющее статус клиента.
 * Возможные значения: NORMAL (обычный), BLACKLISTED (в черном списке) и UNKNOWN (неизвестный).
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
public enum ClientStatus {
    /**
     * Обычный статус клиента.
     */
    NORMAL,

    /**
     * Статус клиента, находящегося в черном списке.
     */
    BLACKLISTED,

    /**
     * Неизвестный статус клиента.
     */
    UNKNOWN
}