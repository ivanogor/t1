package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.service.KafkaProducerService;

/**
 * Реализация сервиса для отправки сообщений в Kafka.
 * Предоставляет методы для отправки сообщений о счетах и транзакциях.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {

    /**
     * Шаблон Kafka для отправки сообщений о счетах.
     */
    private final KafkaTemplate<String, AccountDto> kafkaAccountTemplate;

    /**
     * Шаблон Kafka для отправки сообщений о транзакциях.
     */
    private final KafkaTemplate<String, TransactionDto> kafkaTransactionTemplate;

    @Override
    public void sendAccountMessage(String topic, String key, AccountDto accountDto) {
        sendMessage(kafkaAccountTemplate, topic, key, accountDto);
    }

    @Override
    public void sendTransactionMessage(String topic, String key, TransactionDto transactionDto) {
        sendMessage(kafkaTransactionTemplate, topic, key, transactionDto);
    }

    /**
     * Отправляет сообщение в указанный топик Kafka.
     *
     * @param kafkaTemplate Шаблон Kafka для отправки сообщений.
     * @param topic         Имя топика Kafka.
     * @param key           Ключ сообщения.
     * @param message       Сообщение для отправки.
     * @param <T>           Тип сообщения.
     */
    private <T> void sendMessage(KafkaTemplate<String, T> kafkaTemplate, String topic, String key, T message) {
        kafkaTemplate.send(topic, key, message);
    }
}