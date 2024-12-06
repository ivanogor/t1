package com.ivanogor.service3.enums;

/**
 * Перечисление, представляющее статусы банковских счетов.
 * Каждый статус описывает текущее состояние счета.
 *
 * @author ivanogor
 * @version 1.0
 * @since 7.11.2024
 */
public enum AccountStatus {
    /**
     * Счет арестован.
     */
    ARRESTED,

    /**
     * Счет заблокирован.
     */
    BLOCKED,

    /**
     * Счет закрыт.
     */
    CLOSED,

    /**
     * Счет открыт.
     */
    OPEN
}