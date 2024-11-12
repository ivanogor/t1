package com.demo.service.service.impl;

import com.demo.service.dto.TransactionAcceptedMessageDto;
import com.demo.service.dto.TransactionResultMessageDto;
import com.demo.service.model.TransactionStatus;
import com.demo.service.service.TransactionProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionProcessingServiceImpl implements TransactionProcessingService {

    @Value("${t1.transaction.threshold.count}")
    private int transactionThresholdCount;

    @Value("${t1.transaction.threshold.time}")
    private int transactionThresholdTime;

    private final Map<String, Integer> transactionCountMap = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> transactionTimeMap = new ConcurrentHashMap<>();

    @Override
    public TransactionResultMessageDto processTransaction(TransactionAcceptedMessageDto message) {
        String key = message.getClientId() + "_" + message.getAccountId();
        LocalDateTime now = LocalDateTime.now();

        // Проверка на превышение порога транзакций
        if (transactionCountMap.containsKey(key)) {
            LocalDateTime lastTransactionTime = transactionTimeMap.get(key);
            if (now.isBefore(lastTransactionTime.plusSeconds(transactionThresholdTime))) {
                int count = transactionCountMap.get(key);
                if (count >= transactionThresholdCount) {
                    return new TransactionResultMessageDto(message.getTransactionId(), TransactionStatus.BLOCKED, message.getAccountId());
                }
                transactionCountMap.put(key, count + 1);
            } else {
                transactionCountMap.put(key, 1);
                transactionTimeMap.put(key, now);
            }
        } else {
            transactionCountMap.put(key, 1);
            transactionTimeMap.put(key, now);
        }

        // Проверка на достаточность баланса
        if (message.getAmount().compareTo(message.getBalance()) > 0) {
            return new TransactionResultMessageDto(message.getTransactionId(), TransactionStatus.REJECTED, message.getAccountId());
        }

        // Если всё ок, статус ACCEPTED
        return new TransactionResultMessageDto(message.getTransactionId(), TransactionStatus.ACCEPTED, message.getAccountId());
    }
}