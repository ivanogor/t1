package ru.t1.java.demo.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.TransactionResultMessageDto;
import ru.t1.java.demo.service.TransactionResultService;

/**
 * Компонент для обработки сообщений о результатах транзакций из Kafka.
 * При получении сообщения о результате транзакции, обрабатывает его с помощью сервиса TransactionResultService.
 *
 * @author ivanogor
 * @version 1.0
 * @since 7.11.2024
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionResultConsumer {

    /**
     * Сервис для обработки результатов транзакций.
     */
    private final TransactionResultService transactionResultService;

    /**
     * Метод-слушатель для обработки сообщений о результатах транзакций.
     *
     * @param message Сообщение о результате транзакции.
     */
    @KafkaListener(id = "transactionResultListener",
            topics = "${t1.kafka.topic.transactions_result}",
            containerFactory = "kafkaTransactionalListenerContainerFactory")
    public void listener(TransactionResultMessageDto message) {
        log.info("Received transaction result message: {}", message);

        // Обработка результата транзакции
        transactionResultService.processTransactionResult(message);
    }
}