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

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionAcceptConsumer {

    @Value("${t1.kafka.topic.transactions_result}")
    private String transactionResultTopic;

    private final TransactionProcessingService transactionProcessingService;
    private final KafkaTemplate<String, TransactionResultMessageDto> transactionResultKafkaTemplate;

    @KafkaListener(id = "transactionAcceptListener",
            topics = "${t1.kafka.topic.transactions_accept}",
            containerFactory = "kafkaTransactionAcceptedMessageDtoListenerContainerFactory")
    public void listener(TransactionAcceptedMessageDto message) {
        log.info("Received transaction accepted message: {}", message);

        TransactionResultMessageDto resultMessage = transactionProcessingService.processTransaction(message);

        transactionResultKafkaTemplate.send(transactionResultTopic, resultMessage);
        log.info("Sent transaction result message: {}", resultMessage);
    }
}