package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.exception.AccountNotFoundException;
import ru.t1.java.demo.service.AccountService;

import java.util.List;

/**
 * Контроллер для работы с банковскими счетами.
 * Предоставляет REST API для создания, чтения, обновления и удаления счетов.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
@Slf4j
public class AccountController {

    /**
     * Сервис для работы с банковскими счетами.
     */
    private final AccountService accountService;

    /**
     * Создает новый банковский счет.
     *
     * @param accountDto Данные для создания счета.
     * @return Созданный счет.
     */
    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        log.info("Получен запрос на создание счета: {}", accountDto);
        AccountDto createdAccount = accountService.createAccount(accountDto);
        return ResponseEntity.ok(createdAccount);
    }

    /**
     * Получает банковский счет по его идентификатору.
     *
     * @param id Идентификатор счета.
     * @return Найденный счет или 404 ошибка, если счет не найден.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id) {
        log.info("Получен запрос на получение счета по ID: {}", id);
        try {
            AccountDto foundAccount = accountService.getAccount(id);
            return ResponseEntity.ok(foundAccount);
        }catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Получает все банковские счета.
     *
     * @return Список всех счетов.
     */
    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        log.info("Получен запрос на получение всех счетов");
        List<AccountDto> accounts = accountService.getAccounts();
        return ResponseEntity.ok(accounts);
    }

    /**
     * Обновляет существующий банковский счет.
     *
     * @param id         Идентификатор счета.
     * @param accountDto Данные для обновления счета.
     * @return Обновленный счет или 404 ошибка, если счет не найден.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        log.info("Получен запрос на обновление счета с ID {}: {}", id, accountDto);
        try {
            AccountDto updatedAccount = accountService.updateAccount(id, accountDto);
            return ResponseEntity.ok(updatedAccount);
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Удаляет банковский счет по его идентификатору.
     *
     * @param id Идентификатор счета.
     * @return 200 OK, если счет успешно удален, или 404 ошибка, если счет не найден.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        log.info("Получен запрос на удаление счета с ID: {}", id);
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.ok().build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}