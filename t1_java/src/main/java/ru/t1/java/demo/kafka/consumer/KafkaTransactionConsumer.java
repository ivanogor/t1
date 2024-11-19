package ru.t1.java.demo.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.service.TransactionService;

/**
 * Консьюмер Kafka для обработки сообщений, связанных с регистрацией транзакций.
 * Обрабатывает сообщения из топика Kafka и создает новые транзакции с использованием {@link TransactionService}.
 *
 * @author ivanogor
 * @version 1.0
 * @since 7.11.2024
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionConsumer {

    /**
     * Сервис для работы с транзакциями.
     */
    private final TransactionService transactionService;

    /**
     * Обрабатывает сообщения из топика Kafka, связанные с регистрацией транзакций.
     *
     * @param transactionDto Данные о транзакции, полученные из Kafka.
     * @param ack            Объект для подтверждения обработки сообщения.
     * @param topic          Имя топика Kafka, из которого получено сообщение.
     * @param key            Ключ сообщения в Kafka.
     */
    @KafkaListener(id = "transactionListener",
            topics = "${t1.kafka.topic.transactions_registration}",
            containerFactory = "kafkaTransactionalListenerContainerFactory")
    public void listener(@Payload TransactionDto transactionDto,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("Transaction consumer: Обработка нового сообщения из топика {} с ключом {}", topic, key);

        try {
            transactionService.createTransaction(transactionDto);
        } finally {
            ack.acknowledge();
        }

        log.info("Transaction consumer: сообщение из топика {} с ключом {} обработано", topic, key);
    }
}