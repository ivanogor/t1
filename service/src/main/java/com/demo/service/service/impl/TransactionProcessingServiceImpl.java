package com.demo.service.service.impl;

import com.demo.service.dto.TransactionAcceptedMessageDto;
import com.demo.service.dto.TransactionResultMessageDto;
import com.demo.service.model.enums.TransactionStatus;
import com.demo.service.service.TransactionProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реализация сервиса для обработки транзакций.
 * Обрабатывает принятые транзакции, проверяет их на соответствие определенным условиям и возвращает результат обработки.
 *
 * @author ivanogor
 * @version 1.0
 * @since 7.11.2024
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionProcessingServiceImpl implements TransactionProcessingService {

    /**
     * Пороговое значение количества транзакций, которое может быть выполнено в течение определенного времени.
     */
    @Value("${t1.transaction.threshold.count}")
    private int transactionThresholdCount;

    /**
     * Временной интервал (в секундах), в течение которого проверяется количество транзакций.
     */
    @Value("${t1.transaction.threshold.time}")
    private int transactionThresholdTime;

    /**
     * Карта для хранения количества транзакций для каждого клиента и счета.
     */
    private final Map<String, Integer> transactionCountMap = new ConcurrentHashMap<>();

    /**
     * Карта для хранения времени последней транзакции для каждого клиента и счета.
     */
    private final Map<String, LocalDateTime> transactionTimeMap = new ConcurrentHashMap<>();

    @Override
    public TransactionResultMessageDto processTransaction(TransactionAcceptedMessageDto message) {
        String key = message.getClientId() + "_" + message.getAccountId();
        LocalDateTime now = LocalDateTime.now();

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

        if (message.getAmount().compareTo(message.getBalance()) > 0) {
            return new TransactionResultMessageDto(message.getTransactionId(), TransactionStatus.REJECTED, message.getAccountId());
        }

        return new TransactionResultMessageDto(message.getTransactionId(), TransactionStatus.ACCEPTED, message.getAccountId());
    }
}