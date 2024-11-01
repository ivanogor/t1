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
            log.info("Инициализация сервиса: начато чтение и сохранение данных из JSON");
            List<Account> accounts = parseJson();
            accountRepository.saveAll(accounts);
            log.info("Инициализация сервиса: данные успешно сохранены");
        } catch (IOException e) {
            log.error("Ошибка во время инициализации сервиса", e);
        }
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        log.info("Создание нового счета: начато");
        Account accountToCreate = AccountMapper.toEntity(accountDto);
        Account createdAccount = accountRepository.save(accountToCreate);
        log.info("Создание нового счета: завершено, ID: {}", createdAccount.getId());
        return AccountMapper.toDto(createdAccount);
    }

    @Override
    public AccountDto getAccount(Long id) {
        log.info("Получение счета по ID: {}", id);
        Optional<Account> optionalFoundAccount = accountRepository.findById(id);
        if (optionalFoundAccount.isEmpty()) {
            logAccountNotFound(id);
            throw new AccountNotFoundException(id);
        }
        Account foundAccount = optionalFoundAccount.get();
        log.info("Счет с ID {} успешно получен", id);
        return AccountMapper.toDto(foundAccount);
    }

    @Override
    public List<AccountDto> getAccounts() {
        log.info("Получение всех счетов: начато");
        List<AccountDto> accounts = accountRepository.findAll()
                .stream()
                .map(AccountMapper::toDto)
                .toList();
        log.info("Получение всех счетов: завершено, найдено {} счетов", accounts.size());
        return accounts;
    }

    @Override
    public void deleteAccount(Long id) {
        log.info("Удаление счета по ID: {}", id);
        if (!accountRepository.existsById(id)) {
            logAccountNotFound(id);
            throw new AccountNotFoundException(id);
        }
        accountRepository.deleteById(id);
        log.info("Счет с ID {} успешно удален", id);
    }

    @Override
    public AccountDto updateAccount(Long id, AccountDto updatedAccountDto) {
        log.info("Обновление счета по ID: {}", id);
        Optional<Account> existingOptionalAccount = accountRepository.findById(id);
        if (existingOptionalAccount.isEmpty()) {
            logAccountNotFound(id);
            throw new AccountNotFoundException(id);
        }
        Account existingAccount = existingOptionalAccount.get();
        existingAccount.setAccountType(updatedAccountDto.getAccountType());
        existingAccount.setBalance(updatedAccountDto.getBalance());

        Account updatedAccount = accountRepository.save(existingAccount);
        log.info("Счет с ID {} успешно обновлен", id);
        return AccountMapper.toDto(updatedAccount);
    }

    @Override
    public List<Account> parseJson() throws IOException {
        log.info("Парсинг JSON-файла: начато");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("ACCOUNT_DATA.json");
        if (inputStream == null) {
            log.error("Файл ACCOUNT_DATA.json не найден");
            throw new IOException("Файл ACCOUNT_DATA.json не найден");
        }
        AccountDto[] accounts = mapper.readValue(inputStream, AccountDto[].class);
        List<Account> accountList = Arrays.stream(accounts)
                .map(AccountMapper::toEntity)
                .toList();
        log.info("Парсинг JSON-файла: завершено, найдено {} счетов", accountList.size());
        return accountList;
    }

    /**
     * Логирует сообщение о том, что счет с указанным ID не найден.
     *
     * @param id ID счета, который не был найден.
     */
    private void logAccountNotFound(Long id) {
        log.warn("Счет с ID {} не найден", id);
    }
}