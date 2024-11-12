package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.dto.TransactionAcceptedMessageDto;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.exception.AccountStatusIsNotOpenedException;
import ru.t1.java.demo.exception.TransactionNotFoundException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.AccountStatus;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.TransactionStatus;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.util.TransactionMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с сущностью Transaction.
 * Предоставляет методы для сохранения, получения, обновления и удаления транзакций.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    /**
     * Репозиторий для работы с сущностью Transaction.
     */
    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;

    /**
     * Маппер для преобразования между DTO и сущностью Transaction.
     */
    private final TransactionMapper transactionMapper;

    private final KafkaTemplate<String, TransactionAcceptedMessageDto> kafkaTemplate;

    @Value("${t1.kafka.topic.transactions_accept}")
    private String TRANSACTION_ACCEPTED_TOPIC;

    @Override
    @Transactional
    public TransactionDto createTransaction(TransactionDto transactionDto) {
        log.info("Создание новой транзакции: начато");
        Transaction transactionToCreate = transactionMapper.toEntity(transactionDto);
        Account account = transactionToCreate.getAccount();
        if (account.getAccountStatus() != AccountStatus.OPEN){
            log.error("Transaction rejected: Account status is not open");
            throw new AccountStatusIsNotOpenedException();
        }

        transactionToCreate.setTransactionStatus(TransactionStatus.REQUESTED);
        Transaction createdTransaction = transactionRepository.save(transactionToCreate);

        BigDecimal newBalance = account.getBalance().add(transactionToCreate.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);

        sendTransactionAcceptedMessage(account, createdTransaction);

        log.info("Создание новой транзакции: завершено, ID: {}", createdTransaction.getId());
        return transactionMapper.toDto(createdTransaction);
    }

    @Override
    public TransactionDto getTransaction(Long id) {
        log.info("Получение транзакции по ID: {}", id);
        Optional<Transaction> optionalFoundTransaction = transactionRepository.findById(id);
        if (optionalFoundTransaction.isEmpty()) {
            logTransactionNotFound(id);
            throw new TransactionNotFoundException(id);
        }
        Transaction foundTransaction = optionalFoundTransaction.get();
        log.info("Транзакция с ID {} успешно получена", id);
        return transactionMapper.toDto(foundTransaction);
    }

    @Override
    public List<TransactionDto> getTransactions() {
        log.info("Получение всех транзакций: начато");
        List<TransactionDto> transactions = transactionRepository.findAll()
                .stream()
                .map(transactionMapper::toDto)
                .toList();
        log.info("Получение всех транзакций: завершено, найдено {} транзакций", transactions.size());
        return transactions;
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        log.info("Удаление транзакции по ID: {}", id);
        if (!transactionRepository.existsById(id)) {
            logTransactionNotFound(id);
            throw new TransactionNotFoundException(id);
        }
        transactionRepository.deleteById(id);
        log.info("Транзакция с ID {} успешно удалена", id);
    }

    @Override
    @Transactional
    public TransactionDto updateTransaction(Long id, TransactionDto transactionDto) {
        log.info("Обновление транзакции по ID: {}", id);
        Optional<Transaction> existingOptionalTransaction = transactionRepository.findById(id);
        if (existingOptionalTransaction.isEmpty()) {
            logTransactionNotFound(id);
            throw new TransactionNotFoundException(id);
        }
        Transaction existingTransaction = existingOptionalTransaction.get();
        existingTransaction.setAmount(transactionDto.getAmount());

        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        log.info("Транзакция с ID {} успешно обновлена", id);
        return transactionMapper.toDto(updatedTransaction);
    }

    /**
     * Логирует сообщение о том, что транзакция с указанным ID не найдена.
     *
     * @param id ID транзакции, которая не была найдена.
     */
    private void logTransactionNotFound(Long id) {
        log.warn("Транзакция с ID {} не найдена", id);
    }

    private void sendTransactionAcceptedMessage(Account account, Transaction transaction){
        TransactionAcceptedMessageDto message = TransactionAcceptedMessageDto.builder()
                .clientId(account.getClient().getClientId())
                .accountId(account.getAccountId())
                .transactionId(transaction.getTransactionId())
                .timestamp(transaction.getCreatedAt())
                .amount(transaction.getAmount())
                .balance(account.getBalance())
                .build();

        kafkaTemplate.send(TRANSACTION_ACCEPTED_TOPIC, message);

        log.info("Transaction accepted message sent to Kafka topic '{}' for transaction ID: {}", TRANSACTION_ACCEPTED_TOPIC, transaction.getId());
    }
}