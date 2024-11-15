package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.dto.TransactionResultMessageDto;
import ru.t1.java.demo.exception.TransactionNotFoundException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.enums.AccountStatus;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.enums.TransactionStatus;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.TransactionResultService;

/**
 * Реализация сервиса для обработки результатов транзакций.
 * Обрабатывает результаты транзакций, обновляет статусы транзакций и счетов в зависимости от результата.
 *
 * @author ivanogor
 * @version 1.0
 * @since 7.11.2024
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionResultServiceImpl implements TransactionResultService {

    /**
     * Репозиторий для работы с сущностью Account.
     */
    private final AccountRepository accountRepository;

    /**
     * Репозиторий для работы с сущностью Transaction.
     */
    private final TransactionRepository transactionRepository;


    @Transactional
    public void processTransactionResult(TransactionResultMessageDto message) {
        Transaction transaction = transactionRepository.findByTransactionId(message.getTransactionId())
                .orElseThrow(TransactionNotFoundException::new);

        Account account = transaction.getAccount();

        switch (message.getStatus()) {
            case ACCEPTED ->
                    transaction.setTransactionStatus(TransactionStatus.ACCEPTED);
            case BLOCKED -> {
                transaction.setTransactionStatus(TransactionStatus.BLOCKED);
                account.setAccountStatus(AccountStatus.BLOCKED);
                account.setFrozenAmount(account.getFrozenAmount().add(transaction.getAmount()));
                account.setBalance(account.getBalance().subtract(transaction.getAmount()));
            }
            case REJECTED -> {
                transaction.setTransactionStatus(TransactionStatus.REJECTED);
                account.setBalance(account.getBalance().subtract(transaction.getAmount()));
            }
        }

        transactionRepository.save(transaction);
        accountRepository.save(account);

        log.info("Processed transaction result: {}", message);
    }
}