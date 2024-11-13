package com.demo.service.kafka.consumer;

import com.demo.service.dto.TransactionAcceptedMessageDto;
import com.demo.service.dto.TransactionResultMessageDto;
import com.demo.service.service.TransactionProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Компонент для обработки сообщений о принятых транзакциях из Kafka.
 * При получении сообщения о принятой транзакции, обрабатывает его и отправляет результат обработки в другой топик Kafka.
 *
 * @author ivanogor
 * @version 1.0
 * @since 7.11.2024
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionAcceptConsumer {

    /**
     * Название топика Kafka, в который будут отправляться результаты обработки транзакций.
     */
    @Value("${t1.kafka.topic.transactions_result}")
    private String transactionResultTopic;

    /**
     * Сервис для обработки транзакций.
     */
    private final TransactionProcessingService transactionProcessingService;

    /**
     * Шаблон Kafka для отправки сообщений о результатах транзакций.
     */
    private final KafkaTemplate<String, TransactionResultMessageDto> transactionResultKafkaTemplate;

    /**
     * Метод-слушатель для обработки сообщений о принятых транзакциях.
     *
     * @param message Сообщение о принятой транзакции.
     */
    @KafkaListener(id = "transactionAcceptListener",
            topics = "${t1.kafka.topic.transactions_accept}",
            containerFactory = "kafkaTransactionAcceptedMessageDtoListenerContainerFactory")
    public void listener(TransactionAcceptedMessageDto message) {
        log.info("Received transaction accepted message: {}", message);

        // Обработка транзакции и получение результата
        TransactionResultMessageDto resultMessage = transactionProcessingService.processTransaction(message);

        // Отправка результата обработки в топик Kafka
        transactionResultKafkaTemplate.send(transactionResultTopic, resultMessage);
        log.info("Sent transaction result message: {}", resultMessage);
    }
}