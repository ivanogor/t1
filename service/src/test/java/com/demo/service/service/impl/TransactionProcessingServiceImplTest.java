package com.demo.service.service.impl;

import com.demo.service.dto.TransactionAcceptedMessageDto;
import com.demo.service.dto.TransactionResultMessageDto;
import com.demo.service.model.enums.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TransactionProcessingServiceImplTest {

    @InjectMocks
    private TransactionProcessingServiceImpl transactionProcessingService;

    private TransactionAcceptedMessageDto message;

    @BeforeEach
    void setUp() {
        // Инициализация тестовых данных
        message = TransactionAcceptedMessageDto.builder()
                .transactionId(UUID.randomUUID())
                .clientId(UUID.randomUUID())
                .accountId(UUID.randomUUID())
                .amount(new BigDecimal("100.00"))
                .balance(new BigDecimal("200.00"))
                .timestamp(LocalDateTime.now())
                .build();

        // Установка значений для transactionThresholdCount и transactionThresholdTime через ReflectionTestUtils
        ReflectionTestUtils.setField(transactionProcessingService, "transactionThresholdCount", 3);
        ReflectionTestUtils.setField(transactionProcessingService, "transactionThresholdTime", 60);
    }

    @Test
    void testProcessTransaction_FirstTransaction_Accepted() {
        // Вызов тестируемого метода
        TransactionResultMessageDto result = transactionProcessingService.processTransaction(message);

        // Проверка результатов
        assertEquals(TransactionStatus.ACCEPTED, result.getStatus());
    }

    @Test
    void testProcessTransaction_ExceedThreshold_Blocked() throws NoSuchFieldException, IllegalAccessException {
        // Подготовка данных
        String key = message.getClientId() + "_" + message.getAccountId();
        Map<String, Integer> transactionCountMap = getTransactionCountMap();
        Map<String, LocalDateTime> transactionTimeMap = getTransactionTimeMap();

        transactionCountMap.put(key, 3);
        transactionTimeMap.put(key, LocalDateTime.now());

        // Вызов тестируемого метода
        TransactionResultMessageDto result = transactionProcessingService.processTransaction(message);

        // Проверка результатов
        assertEquals(TransactionStatus.BLOCKED, result.getStatus());
    }

    @Test
    void testProcessTransaction_AmountExceedsBalance_Rejected() {
        // Подготовка данных
        message.setAmount(new BigDecimal("300.00"));

        // Вызов тестируемого метода
        TransactionResultMessageDto result = transactionProcessingService.processTransaction(message);

        // Проверка результатов
        assertEquals(TransactionStatus.REJECTED, result.getStatus());
    }

    @Test
    void testProcessTransaction_WithinThreshold_Accepted() throws NoSuchFieldException, IllegalAccessException {
        // Подготовка данных
        String key = message.getClientId() + "_" + message.getAccountId();
        Map<String, Integer> transactionCountMap = getTransactionCountMap();
        Map<String, LocalDateTime> transactionTimeMap = getTransactionTimeMap();

        transactionCountMap.put(key, 2);
        transactionTimeMap.put(key, LocalDateTime.now());

        // Вызов тестируемого метода
        TransactionResultMessageDto result = transactionProcessingService.processTransaction(message);

        // Проверка результатов
        assertEquals(TransactionStatus.ACCEPTED, result.getStatus());
    }

    @Test
    void testProcessTransaction_TimeExceeded_Accepted() throws NoSuchFieldException, IllegalAccessException {
        // Подготовка данных
        String key = message.getClientId() + "_" + message.getAccountId();
        Map<String, Integer> transactionCountMap = getTransactionCountMap();
        Map<String, LocalDateTime> transactionTimeMap = getTransactionTimeMap();

        transactionCountMap.put(key, 3);
        transactionTimeMap.put(key, LocalDateTime.now().minusSeconds(61));

        // Вызов тестируемого метода
        TransactionResultMessageDto result = transactionProcessingService.processTransaction(message);

        // Проверка результатов
        assertEquals(TransactionStatus.ACCEPTED, result.getStatus());
    }

    private Map<String, Integer> getTransactionCountMap() throws NoSuchFieldException, IllegalAccessException {
        Field field = TransactionProcessingServiceImpl.class.getDeclaredField("transactionCountMap");
        field.setAccessible(true);
        return (Map<String, Integer>) field.get(transactionProcessingService);
    }

    private Map<String, LocalDateTime> getTransactionTimeMap() throws NoSuchFieldException, IllegalAccessException {
        Field field = TransactionProcessingServiceImpl.class.getDeclaredField("transactionTimeMap");
        field.setAccessible(true);
        return (Map<String, LocalDateTime>) field.get(transactionProcessingService);
    }
}