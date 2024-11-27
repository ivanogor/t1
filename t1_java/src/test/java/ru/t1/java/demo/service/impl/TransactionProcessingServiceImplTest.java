package ru.t1.java.demo.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.t1.java.demo.dto.TransactionResultMessageDto;
import ru.t1.java.demo.exception.TransactionNotFoundException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.enums.AccountStatus;
import ru.t1.java.demo.model.enums.TransactionStatus;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionProcessingServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionProcessingServiceImpl transactionProcessingService;

    private Transaction transaction;
    private Account account;
    private TransactionResultMessageDto message;

    @BeforeEach
    public void setUp() {
        transaction = new Transaction();
        transaction.setTransactionId(UUID.randomUUID());
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setTransactionStatus(TransactionStatus.ACCEPTED);

        account = new Account();
        account.setAccountStatus(AccountStatus.OPEN);
        account.setBalance(BigDecimal.valueOf(1000));
        account.setFrozenAmount(BigDecimal.ZERO);

        transaction.setAccount(account);

        message = new TransactionResultMessageDto();
        message.setTransactionId(UUID.randomUUID());
        message.setStatus(TransactionStatus.ACCEPTED);

        // Устанавливаем значение rejectedThreshold через ReflectionTestUtils
        ReflectionTestUtils.setField(transactionProcessingService, "rejectedThreshold", 3);
    }

    @Test
    public void testProcessTransaction_Accepted() {
        when(transactionRepository.findByTransactionId(any())).thenReturn(Optional.of(transaction));

        transactionProcessingService.processTransaction(message, false);

        verify(transactionRepository).save(transaction);
        verify(accountRepository).save(account);

        assertEquals(TransactionStatus.ACCEPTED, transaction.getTransactionStatus());
        assertEquals(BigDecimal.valueOf(1000), account.getBalance());
        assertEquals(AccountStatus.OPEN, account.getAccountStatus());
    }

    @Test
    public void testProcessTransaction_Rejected() {
        message.setStatus(TransactionStatus.REJECTED);
        when(transactionRepository.findByTransactionId(any())).thenReturn(Optional.of(transaction));
        when(transactionRepository.countByAccount_IdAndTransactionStatus(any(), any())).thenReturn(2L);

        transactionProcessingService.processTransaction(message, false);

        verify(transactionRepository).save(transaction);
        verify(accountRepository).save(account);

        assertEquals(TransactionStatus.REJECTED, transaction.getTransactionStatus());
        assertEquals(BigDecimal.valueOf(900), account.getBalance());
        assertEquals(AccountStatus.OPEN, account.getAccountStatus());
    }

    @Test
    public void testProcessTransaction_Rejected_ExceedsThreshold() {
        message.setStatus(TransactionStatus.REJECTED);
        when(transactionRepository.findByTransactionId(any())).thenReturn(Optional.of(transaction));
        when(transactionRepository.countByAccount_IdAndTransactionStatus(any(), any())).thenReturn(3L);

        transactionProcessingService.processTransaction(message, false);

        verify(transactionRepository).save(transaction);
        verify(accountRepository).save(account);

        assertEquals(TransactionStatus.REJECTED, transaction.getTransactionStatus());
        assertEquals(BigDecimal.valueOf(900), account.getBalance());
        assertEquals(AccountStatus.ARRESTED, account.getAccountStatus());
    }

    @Test
    public void testProcessTransaction_Blacklisted() {
        when(transactionRepository.findByTransactionId(any())).thenReturn(Optional.of(transaction));

        transactionProcessingService.processTransaction(message, true);

        verify(transactionRepository).save(transaction);
        verify(accountRepository).save(account);

        assertEquals(TransactionStatus.REJECTED, transaction.getTransactionStatus());
        assertEquals(BigDecimal.valueOf(900), account.getBalance());
        assertEquals(BigDecimal.valueOf(100), account.getFrozenAmount());
        assertEquals(AccountStatus.BLOCKED, account.getAccountStatus());
    }

    @Test
    public void testProcessTransaction_TransactionNotFound() {
        when(transactionRepository.findByTransactionId(any())).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionProcessingService.processTransaction(message, false));
    }
}