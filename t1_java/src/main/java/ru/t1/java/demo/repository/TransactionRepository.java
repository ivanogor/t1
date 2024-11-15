package ru.t1.java.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.t1.java.demo.model.Transaction;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью Transaction.
 * Предоставляет базовые операции CRUD (создание, чтение, обновление, удаление) для транзакций.
 *
 * @author ivanogor
 * @version 2.0
 * @since 7.11.2024
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
}