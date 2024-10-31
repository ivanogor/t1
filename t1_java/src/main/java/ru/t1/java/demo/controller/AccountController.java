package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.exception.AccountNotFoundException;
import ru.t1.java.demo.model.Account;
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
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto accountDto) {
        Account createdAccount = accountService.createAccount(accountDto);
        return ResponseEntity.ok(createdAccount);
    }

    /**
     * Получает банковский счет по его идентификатору.
     *
     * @param id Идентификатор счета.
     * @return Найденный счет или 404 ошибка, если счет не найден.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return accountService.getAccount(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Получает все банковские счета.
     *
     * @return Список всех счетов.
     */
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAccounts();
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
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        try {
            Account updatedAccount = accountService.updateAccount(id, accountDto);
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
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.ok().build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}