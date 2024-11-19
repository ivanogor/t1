package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.dto.TransactionDto;

/**
 * Сервис для отправки сообщений в Kafka.
 * Предоставляет методы для отправки сообщений о счетах и транзакциях.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
public interface KafkaProducerService {

    /**
     * Отправляет сообщение о счете в указанный топик Kafka.
     *
     * @param topic      Имя топика Kafka.
     * @param key        Ключ сообщения.
     * @param accountDto Данные о счете.
     */
    void sendAccountMessage(String topic, String key, AccountDto accountDto);

    /**
     * Отправляет сообщение о транзакции в указанный топик Kafka.
     *
     * @param topic          Имя топика Kafka.
     * @param key            Ключ сообщения.
     * @param transactionDto Данные о транзакции.
     */
    void sendTransactionMessage(String topic, String key, TransactionDto transactionDto);
}
