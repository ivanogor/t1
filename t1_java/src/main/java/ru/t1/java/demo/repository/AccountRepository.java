package ru.t1.java.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.enums.AccountStatus;

import java.util.List;

/**
 * Репозиторий для работы с сущностью Account.
 * Предоставляет базовые операции CRUD (создание, чтение, обновление, удаление) для банковских счетов.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    long countAccountByAccountStatus(AccountStatus status);
    @Query(value = "SELECT * FROM account WHERE account_status = 'BLOCKED' LIMIT :limit", nativeQuery = true)
    List<Account> findTopMBlockedAccounts(@Param("limit") int limit);
}