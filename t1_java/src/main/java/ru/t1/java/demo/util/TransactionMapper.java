package ru.t1.java.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.exception.AccountNotFoundException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.AccountRepository;

/**
 * Маппер для преобразования между DTO и сущностью Transaction.
 * Предоставляет методы для конвертации TransactionDto в Transaction и обратно.
 *
 * @author ivanogor
 * @version 1.0
 * @since 7.11.2024
 */
@RequiredArgsConstructor
@Component
public class TransactionMapper {

    /**
     * Репозиторий для работы с сущностью Account.
     */
    private final AccountRepository accountRepository;

    /**
     * Преобразует DTO в сущность Transaction.
     *
     * @param transactionDto DTO с данными для создания сущности Transaction.
     * @return Сущность Transaction.
     * @throws AccountNotFoundException если счет с указанным ID не найден.
     */
    public Transaction toEntity(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDto.getAmount());
        long id = transactionDto.getAccountId();
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        transaction.setAccount(account);
        Transaction.builder()
                .amount(transactionDto.getAmount())
                .account(account)
                .build();

        return transaction;
    }

    /**
     * Преобразует сущность Transaction в DTO.
     *
     * @param transaction Сущность Transaction.
     * @return DTO с данными сущности Transaction.
     */
    public TransactionDto toDto(Transaction transaction) {
        Account account = transaction.getAccount();
        Long accountId = account != null ? account.getId() : null;

        return TransactionDto.builder()
                .id(transaction.getId())
                .accountId(accountId)
                .amount(transaction.getAmount())
                .transactionStatus(transaction.getTransactionStatus())
                .createdAt(transaction.getCreatedAt())
                .transactionId(transaction.getTransactionId())
                .build();
    }
}