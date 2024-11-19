package ru.t1.java.demo.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.service.AccountService;

/**
 * Консьюмер Kafka для обработки сообщений, связанных с регистрацией банковских счетов.
 * Обрабатывает сообщения из топика Kafka и создает новые счета с использованием {@link AccountService}.
 *
 * @author ivanogor
 * @version 1.0
 * @since 7.11.2024
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaAccountConsumer {

    /**
     * Сервис для работы с банковскими счетами.
     */
    private final AccountService accountService;

    /**
     * Обрабатывает сообщения из топика Kafka, связанные с регистрацией банковских счетов.
     *
     * @param accountDto Данные о счете, полученные из Kafka.
     * @param ack        Объект для подтверждения обработки сообщения.
     * @param topic      Имя топика Kafka, из которого получено сообщение.
     * @param key        Ключ сообщения в Kafka.
     */
    @KafkaListener(id = "accountListener",
            topics = "${t1.kafka.topic.account_registration}",
            containerFactory = "kafkaAccountListenerContainerFactory")
    public void listener(@Payload AccountDto accountDto,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("Account consumer: Обработка нового сообщения из топика {} с ключом {}", topic, key);

        try {
            accountService.createAccount(accountDto);
        } finally {
            ack.acknowledge();
        }

        log.info("Account consumer: сообщение из топика {} с ключом {} обработано", topic, key);
    }
}