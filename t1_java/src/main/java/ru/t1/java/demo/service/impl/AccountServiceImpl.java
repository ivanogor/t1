package ru.t1.java.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.exception.AccountNotFoundException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.util.AccountMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с сущностью Account.
 * Предоставляет методы для сохранения, получения, обновления и удаления банковских счетов.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    /**
     * Репозиторий для работы с сущностью Account.
     */
    private final AccountRepository accountRepository;

    /**
     * Инициализация сервиса.
     * Парсит JSON-файл и сохраняет полученные данные в базу данных.
     */
    @PostConstruct
    void init() {
        try {
            List<Account> accounts = parseJson();
            accountRepository.saveAll(accounts);
        } catch (IOException e) {
            log.error("Ошибка во время обработки записей", e);
        }
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account accountToCreate = AccountMapper.toEntity(accountDto);
        Account createdAccount = accountRepository.save(accountToCreate);
        return AccountMapper.toDto(createdAccount);
    }

    @Override
    public AccountDto getAccount(Long id) {
        Optional<Account> optionalFoundAccount = accountRepository.findById(id);
        if (optionalFoundAccount.isEmpty()) {
            throw new AccountNotFoundException(id);
        }
        Account foundAccount = optionalFoundAccount.get();
        return AccountMapper.toDto(foundAccount);
    }

    @Override
    public List<AccountDto> getAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(AccountMapper::toDto)
                .toList();
    }

    @Override
    public void deleteAccount(Long id) {
        if(!accountRepository.existsById(id)){
            throw new AccountNotFoundException(id);
        }
        accountRepository.deleteById(id);
    }

    @Override
    public AccountDto updateAccount(Long id, AccountDto updatedAccountDto) {
        Account accountToUpdate = AccountMapper.toEntity(updatedAccountDto);
        Optional<Account> existingOptionalAccount = accountRepository.findById(id);
        if(existingOptionalAccount.isEmpty()) {
            throw new AccountNotFoundException(id);
        }
        Account existingAccount = existingOptionalAccount.get();
        existingAccount.setAccountType(accountToUpdate.getAccountType());
        existingAccount.setBalance(accountToUpdate.getBalance());

        Account updatedAccount = accountRepository.save(existingAccount);

        return AccountMapper.toDto(updatedAccount);
    }

    @Override
    public List<Account> parseJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("ACCOUNT_DATA.json");
        if (inputStream == null) {
            throw new IOException("Файл ACCOUNT_DATA.json не найден");
        }
        AccountDto[] accounts = mapper.readValue(inputStream, AccountDto[].class);
        return Arrays.stream(accounts)
                .map(AccountMapper::toEntity)
                .toList();
    }
}
