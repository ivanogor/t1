package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.TransactionResultMessageDto;
import ru.t1.java.demo.model.enums.ClientStatus;
import ru.t1.java.demo.service.ClientStatusService;
import ru.t1.java.demo.service.TransactionProcessingService;
import ru.t1.java.demo.service.TransactionResultService;

/**
 * Реализация сервиса для обработки результатов транзакций.
 * Обрабатывает сообщения о результатах транзакций и определяет, находится ли клиент в черном списке.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionResultServiceImpl implements TransactionResultService {

    private final ClientStatusService clientStatusService;
    private final TransactionProcessingService transactionProcessingService;

    /**
     * Обрабатывает результат транзакции.
     *
     * @param message Объект сообщения с результатом транзакции.
     */
    @Override
    public void processTransactionResult(TransactionResultMessageDto message) {
        clientStatusService.getClientStatus(message.getAccountId().toString())
                .subscribe(clientStatus -> {
                    boolean isBlacklisted = clientStatus != null && clientStatus.getClientStatus().equals(ClientStatus.BLACKLISTED);
                    transactionProcessingService.processTransaction(message, isBlacklisted);
                });
    }
}