package ru.t1.java.demo.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.exception.AccountNotFoundException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.enums.AccountType;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.ClientRepository;
import ru.t1.java.demo.util.AccountMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountDto accountDto;
    private Account account;
    private Client client;

    @BeforeEach
    public void setUp() {
        client = new Client();
        client.setClientId(UUID.randomUUID());

        accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setAccountType(AccountType.CREDIT);
        accountDto.setBalance(BigDecimal.valueOf(1000));
        accountDto.setClientId(1L);

        account = new Account();
        account.setAccountType(AccountType.CREDIT);
        account.setBalance(BigDecimal.valueOf(1000));
        account.setClient(client);
    }

    @Test
    public void testCreateAccount() {
        when(accountMapper.toEntity(accountDto)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        AccountDto createdAccountDto = accountService.createAccount(accountDto);

        assertNotNull(createdAccountDto);
        assertEquals(accountDto.getId(), createdAccountDto.getId());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    public void testGetAccount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        AccountDto foundAccountDto = accountService.getAccount(1L);

        assertNotNull(foundAccountDto);
        assertEquals(accountDto.getId(), foundAccountDto.getId());
    }

    @Test
    public void testGetAccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(1L));
    }

    @Test
    public void testGetAccounts() {
        when(accountRepository.findAll()).thenReturn(List.of(account));
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        List<AccountDto> accounts = accountService.getAccounts();

        assertNotNull(accounts);
        assertEquals(1, accounts.size());
        assertEquals(accountDto.getId(), accounts.get(0).getId());
    }

    @Test
    public void testDeleteAccount() {
        when(accountRepository.existsById(1L)).thenReturn(true);

        accountService.deleteAccount(1L);

        verify(accountRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteAccountNotFound() {
        when(accountRepository.existsById(1L)).thenReturn(false);

        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(1L));
    }

    @Test
    public void testUpdateAccount() {
        AccountDto updatedAccountDto = new AccountDto();
        updatedAccountDto.setId(1L);
        updatedAccountDto.setAccountType(AccountType.DEBIT);
        updatedAccountDto.setBalance(BigDecimal.valueOf(2000));
        updatedAccountDto.setClientId(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.toDto(account)).thenReturn(updatedAccountDto);

        AccountDto result = accountService.updateAccount(1L, updatedAccountDto);

        assertNotNull(result);
        assertEquals(updatedAccountDto.getAccountType(), result.getAccountType());
        assertEquals(updatedAccountDto.getBalance(), result.getBalance());
    }

    @Test
    public void testUpdateAccountNotFound() {
        AccountDto updatedAccountDto = new AccountDto();
        updatedAccountDto.setId(1L);
        updatedAccountDto.setAccountType(AccountType.DEBIT);
        updatedAccountDto.setBalance(BigDecimal.valueOf(2000));
        updatedAccountDto.setClientId(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.updateAccount(1L, updatedAccountDto));
    }
}