package com.demo.service.model.enums;

/**
 * Перечисление, представляющее статус клиента.
 * Возможные значения: NORMAL (обычный) и BLACKLISTED (в черном списке).
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
    BLACKLISTED
}