package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.model.Transaction;

import java.util.List;
import java.util.Optional;

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
    Transaction createTransaction(TransactionDto transactionDto);

    /**
     * Получает транзакцию по её идентификатору.
     *
     * @param id идентификатор транзакции
     * @return Optional, содержащий транзакцию, если она найдена, иначе пустой Optional
     */
    Optional<Transaction> getTransaction(Long id);

    /**
     * Получает список всех транзакций в системе.
     *
     * @return список всех транзакций
     */
    List<Transaction> getTransactions();

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
    Transaction updateTransaction(Long id, TransactionDto transactionDto);
}