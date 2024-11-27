package ru.t1.java.demo.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.t1.java.demo.dto.ClientStatusResponseDto;
import ru.t1.java.demo.dto.TransactionResultMessageDto;
import ru.t1.java.demo.model.enums.ClientStatus;
import ru.t1.java.demo.service.ClientStatusService;
import ru.t1.java.demo.service.TransactionProcessingService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionResultServiceImplTest {

    @Mock
    private ClientStatusService clientStatusService;

    @Mock
    private TransactionProcessingService transactionProcessingService;

    @InjectMocks
    private TransactionResultServiceImpl transactionResultService;

    private TransactionResultMessageDto message;

    @BeforeEach
    public void setUp() {
        message = new TransactionResultMessageDto();
        message.setTransactionId(UUID.randomUUID());
        message.setAccountId(UUID.randomUUID());
    }

    @Test
    public void testProcessTransactionResult_Blacklisted() {
        // Подготовка данных
        ClientStatusResponseDto blacklistedStatus = new ClientStatusResponseDto();
        blacklistedStatus.setClientStatus(ClientStatus.BLACKLISTED);
        doReturn(Mono.just(blacklistedStatus)).when(clientStatusService).getClientStatus(any());

        // Вызов тестируемого метода
        StepVerifier.create(Mono.fromRunnable(() -> transactionResultService.processTransactionResult(message)))
                .then(() -> verify(transactionProcessingService).processTransaction(message, true))
                .verifyComplete();
    }

    @Test
    public void testProcessTransactionResult_NotBlacklisted() {
        // Подготовка данных
        ClientStatusResponseDto normalStatus = new ClientStatusResponseDto();
        normalStatus.setClientStatus(ClientStatus.NORMAL);
        doReturn(Mono.just(normalStatus)).when(clientStatusService).getClientStatus(any());

        // Вызов тестируемого метода
        StepVerifier.create(Mono.fromRunnable(() -> transactionResultService.processTransactionResult(message)))
                .then(() -> verify(transactionProcessingService).processTransaction(message, false))
                .verifyComplete();
    }
}