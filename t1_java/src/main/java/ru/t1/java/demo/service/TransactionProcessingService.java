package ru.t1.java.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.dto.TransactionResultMessageDto;

/**
 * Интерфейс сервиса для обработки транзакций.
 * Предоставляет метод для обработки результатов транзакций и обновления статусов счетов и транзакций.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
public interface TransactionProcessingService {

    /**
     * Обрабатывает результат транзакции.
     *
     * @param message     Объект сообщения с результатом транзакции.
     * @param isBlacklisted Флаг, указывающий, находится ли клиент в черном списке.
     */
    @Transactional
    void processTransaction(TransactionResultMessageDto message, boolean isBlacklisted);
}