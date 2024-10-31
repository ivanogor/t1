package ru.t1.java.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.t1.java.demo.model.Transaction;

/**
 * Репозиторий для работы с сущностью Transaction.
 * Предоставляет базовые операции CRUD (создание, чтение, обновление, удаление) для транзакций.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}