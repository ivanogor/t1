package ru.t1.java.demo.kafka.testRunner;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.service.KafkaProducerService;
import ru.t1.java.demo.model.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TestRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

    private static final String ACCOUNTS_TOPIC = "t1_demo_accounts";
    private static final String TRANSACTIONS_TOPIC = "t1_demo_transactions";

    private final KafkaProducerService kafkaProducerService;

    @Override
    public void run(String... args) {
        sendAccountMessages();
        sendTransactionMessages();
    }

    private void sendAccountMessages() {
        AccountDto accountDto1 = createAccountDto(AccountType.CREDIT, new BigDecimal("1000.00"), 101L);
        AccountDto accountDto2 = createAccountDto(AccountType.DEBIT, new BigDecimal("2000.00"), 102L);

        sendMessage(ACCOUNTS_TOPIC, accountDto1);
        sendMessage(ACCOUNTS_TOPIC, accountDto2);
    }

    private void sendTransactionMessages() {
        TransactionDto transactionDto1 = createTransactionDto(new BigDecimal("1000.00"), 2L);
        TransactionDto transactionDto2 = createTransactionDto(new BigDecimal("1500.00"), 1L);

        sendMessage(TRANSACTIONS_TOPIC, transactionDto1);
        sendMessage(TRANSACTIONS_TOPIC, transactionDto2);
    }

    private AccountDto createAccountDto(AccountType accountType, BigDecimal balance, Long clientId) {
        return AccountDto.builder()
                .accountType(accountType)
                .balance(balance)
                .clientId(clientId)
                .build();
    }

    private TransactionDto createTransactionDto(BigDecimal amount, Long accountId) {
        return TransactionDto.builder()
                .amount(amount)
                .accountId(accountId)
                .build();
    }

    private <T> void sendMessage(String topic, T message) {
        String key = UUID.randomUUID().toString();
        logger.info("Sending message to topic '{}' with key '{}'", topic, key);
        if (message instanceof AccountDto) {
            kafkaProducerService.sendAccountMessage(topic, key, (AccountDto) message);
        } else if (message instanceof TransactionDto) {
            kafkaProducerService.sendTransactionMessage(topic, key, (TransactionDto) message);
        }
    }
}