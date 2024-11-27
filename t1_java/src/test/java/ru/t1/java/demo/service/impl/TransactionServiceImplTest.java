package ru.t1.java.demo.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import ru.t1.java.demo.dto.TransactionAcceptedMessageDto;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.exception.AccountStatusIsNotOpenedException;
import ru.t1.java.demo.exception.TransactionNotFoundException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.enums.AccountStatus;
import ru.t1.java.demo.model.enums.TransactionStatus;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.util.TransactionMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private KafkaTemplate<String, TransactionAcceptedMessageDto> kafkaTemplate;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private TransactionDto transactionDto;
    private Transaction transaction;
    private Account account;

    @BeforeEach
    public void setUp() {
        transactionDto = new TransactionDto();
        transactionDto.setAmount(BigDecimal.valueOf(100));

        transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setTransactionStatus(TransactionStatus.REQUESTED);
        transaction.setCreatedAt(LocalDateTime.now());

        account = new Account();
        account.setAccountId(UUID.randomUUID());
        account.setBalance(BigDecimal.valueOf(1000));
        account.setAccountStatus(AccountStatus.OPEN);

        Client client = new Client();
        client.setClientId(UUID.randomUUID());
        account.setClient(client);

        transaction.setAccount(account);

        ReflectionTestUtils.setField(transactionService, "TRANSACTION_ACCEPTED_TOPIC", "test-topic");
    }

    @Test
    public void testCreateTransaction() {
        when(transactionMapper.toEntity(transactionDto)).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(transactionMapper.toDto(transaction)).thenReturn(transactionDto);

        TransactionDto createdTransactionDto = transactionService.createTransaction(transactionDto);

        assertNotNull(createdTransactionDto);
        assertEquals(transactionDto.getAmount(), createdTransactionDto.getAmount());
        verify(kafkaTemplate, times(1)).send(anyString(), any(TransactionAcceptedMessageDto.class));
    }

    @Test
    public void testCreateTransaction_AccountStatusIsNotOpen() {
        account.setAccountStatus(AccountStatus.CLOSED);
        transaction.setAccount(account);

        when(transactionMapper.toEntity(transactionDto)).thenReturn(transaction);

        assertThrows(AccountStatusIsNotOpenedException.class, () -> transactionService.createTransaction(transactionDto));
    }

    @Test
    public void testGetTransaction() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(transactionDto);

        TransactionDto foundTransactionDto = transactionService.getTransaction(1L);

        assertNotNull(foundTransactionDto);
        assertEquals(transactionDto.getAmount(), foundTransactionDto.getAmount());
    }

    @Test
    public void testGetTransaction_NotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransaction(1L));
    }

    @Test
    public void testGetTransactions() {
        when(transactionRepository.findAll()).thenReturn(List.of(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(transactionDto);

        List<TransactionDto> transactions = transactionService.getTransactions();

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(transactionDto.getAmount(), transactions.get(0).getAmount());
    }

    @Test
    public void testDeleteTransaction() {
        when(transactionRepository.existsById(1L)).thenReturn(true);

        transactionService.deleteTransaction(1L);

        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteTransaction_NotFound() {
        when(transactionRepository.existsById(1L)).thenReturn(false);

        assertThrows(TransactionNotFoundException.class, () -> transactionService.deleteTransaction(1L));
    }

    @Test
    public void testUpdateTransaction() {
        TransactionDto updatedTransactionDto = new TransactionDto();
        updatedTransactionDto.setAmount(BigDecimal.valueOf(200));

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toDto(transaction)).thenReturn(updatedTransactionDto);

        TransactionDto resultTransactionDto = transactionService.updateTransaction(1L, updatedTransactionDto);

        assertNotNull(resultTransactionDto);
        assertEquals(updatedTransactionDto.getAmount(), resultTransactionDto.getAmount());
    }

    @Test
    public void testUpdateTransaction_NotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.updateTransaction(1L, transactionDto));
    }
}