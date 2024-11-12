package ru.t1.java.demo.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.TransactionResultMessageDto;
import ru.t1.java.demo.service.TransactionResultService;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionResultConsumer {

    private final TransactionResultService transactionResultService;

    @KafkaListener(id = "transactionResultListener",
            topics = "${t1.kafka.topic.transactions_result}",
            containerFactory = "kafkaTransactionalListenerContainerFactory")
    public void listener(TransactionResultMessageDto message) {
        log.info("Received transaction result message: {}", message);

        transactionResultService.processTransactionResult(message);
    }
}