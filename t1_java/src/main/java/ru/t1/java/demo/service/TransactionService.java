package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.TransactionDto;

import java.util.List;

/**
 * Интерфейс сервиса для работы с сущностью Transaction.
 * Предоставляет методы для сохранения, получения, обновления и удаления транзакций.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
public interface TransactionService {

    /**
     * Сохраняет новую транзакцию в системе.
     *
     * @param transactionDto транзакция для сохранения
     * @return сохраненная транзакция
     */
    TransactionDto createTransaction(TransactionDto transactionDto);

    /**
     * Получает транзакцию по её идентификатору.
     *
     * @param id идентификатор транзакции
     * @return Optional, содержащий транзакцию, если она найдена, иначе пустой Optional
     */
    TransactionDto getTransaction(Long id);

    /**
     * Получает список всех транзакций в системе.
     *
     * @return список всех транзакций
     */
    List<TransactionDto> getTransactions();

    /**
     * Удаляет транзакцию по её идентификатору.
     *
     * @param id идентификатор транзакции для удаления
     */
    void deleteTransaction(Long id);

    /**
     * Обновляет информацию о транзакции.
     *
     * @param transactionDto транзакция с обновленными данными
     * @param id идентификатор транзакции
     * @return обновленная транзакция
     */
    TransactionDto updateTransaction(Long id, TransactionDto transactionDto);
}