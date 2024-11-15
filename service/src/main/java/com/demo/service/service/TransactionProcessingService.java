package com.demo.service.service;

import com.demo.service.dto.TransactionAcceptedMessageDto;
import com.demo.service.dto.TransactionResultMessageDto;

/**
 * Интерфейс для сервиса обработки транзакций.
 * Предоставляет метод для обработки принятых транзакций и возврата результата обработки.
 *
 * @author ivanogor
 * @version 1.0
 * @since 7.11.2024
 */
public interface TransactionProcessingService {

    /**
     * Обрабатывает принятую транзакцию и возвращает результат обработки.
     *
     * @param message Сообщение о принятой транзакции.
     * @return Результат обработки транзакции.
     */
    TransactionResultMessageDto processTransaction(TransactionAcceptedMessageDto message);
}