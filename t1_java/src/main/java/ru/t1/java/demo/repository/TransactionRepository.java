package ru.t1.java.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.enums.TransactionStatus;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью Transaction.
 * Предоставляет базовые операции CRUD (создание, чтение, обновление, удаление) для транзакций.
 *
 * @author ivanogor
 * @version 3.0
 * @since 21.11.2024
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Находит транзакцию по её уникальному идентификатору.
     *
     * @param transactionId Уникальный идентификатор транзакции.
     * @return Optional, содержащий найденную транзакцию, или пустой, если транзакция не найдена.
     */
    Optional<Transaction> findByTransactionId(UUID transactionId);

    /**
     * Подсчитывает количество транзакций в заданном статусе для указанного счета.
     *
     * @param accountId Идентификатор счета.
     * @param transactionStatus Статус транзакции.
     * @return Количество транзакций в заданном статусе для указанного счета.
     */
    long countByAccount_IdAndTransactionStatus(Long accountId, TransactionStatus transactionStatus);
}