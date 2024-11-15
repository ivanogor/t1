package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.TransactionResultMessageDto;

/**
 * Интерфейс для сервиса обработки результатов транзакций.
 * Предоставляет метод для обработки результатов транзакций и обновления статусов транзакций и счетов.
 *
 * @author ivanogor
 * @version 1.0
 * @since 7.11.2024
 */
public interface TransactionResultService {
    /**
     * Обрабатывает результат транзакции, обновляет статус транзакции и счета в зависимости от результата.
     *
     * @param message Сообщение о результате транзакции.
     */
    void processTransactionResult(TransactionResultMessageDto message);
}
