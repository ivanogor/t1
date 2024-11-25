package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import ru.t1.java.demo.service.TransactionProcessingService;

/**
 * Реализация сервиса для обработки транзакций.
 * Обрабатывает результаты транзакций и обновляет статусы счетов и транзакций.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionProcessingServiceImpl implements TransactionProcessingService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Value("${transaction.rejected.threshold}")
    private int rejectedThreshold;

    /**
     * Обрабатывает результат транзакции.
     *
     * @param message     Объект сообщения с результатом транзакции.
     * @param isBlacklisted Флаг, указывающий, находится ли клиент в черном списке.
     * @throws TransactionNotFoundException Если транзакция не найдена.
     */
    @Transactional
    @Override
    public void processTransaction(TransactionResultMessageDto message, boolean isBlacklisted) {
        Transaction transaction = transactionRepository.findByTransactionId(message.getTransactionId())
                .orElseThrow(TransactionNotFoundException::new);

        Account account = transaction.getAccount();

        if (isBlacklisted) {
            transaction.setTransactionStatus(TransactionStatus.REJECTED);
            account.setAccountStatus(AccountStatus.BLOCKED);
            account.setFrozenAmount(account.getFrozenAmount().add(transaction.getAmount()));
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        } else {
            switch (message.getStatus()) {
                case ACCEPTED ->
                        transaction.setTransactionStatus(TransactionStatus.ACCEPTED);
                case REJECTED -> {
                    transaction.setTransactionStatus(TransactionStatus.REJECTED);
                    account.setBalance(account.getBalance().subtract(transaction.getAmount()));

                    // Проверка количества транзакций в статусе REJECTED
                    long rejectedCount = transactionRepository.countByAccount_IdAndTransactionStatus(account.getId(), TransactionStatus.REJECTED);
                    if (rejectedCount >= rejectedThreshold) {
                        account.setAccountStatus(AccountStatus.ARRESTED);
                    }
                }
            }
        }

        transactionRepository.save(transaction);
        accountRepository.save(account);

        log.info("Processed transaction result: {}", message);
    }
}