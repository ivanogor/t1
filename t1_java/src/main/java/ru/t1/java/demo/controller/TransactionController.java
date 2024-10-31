package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.exception.TransactionNotFoundException;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.service.TransactionService;

import java.util.List;

/**
 * Контроллер для работы с транзакциями.
 * Предоставляет REST API для создания, чтения, обновления и удаления транзакций.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    /**
     * Сервис для работы с транзакциями.
     */
    private final TransactionService transactionService;

    /**
     * Создает новую транзакцию.
     *
     * @param transactionDto Данные для создания транзакции.
     * @return Созданная транзакция.
     */
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDto transactionDto) {
        Transaction createdTransaction = transactionService.createTransaction(transactionDto);
        return ResponseEntity.ok(createdTransaction);
    }

    /**
     * Получает транзакцию по её идентификатору.
     *
     * @param id Идентификатор транзакции.
     * @return Найденная транзакция или 404 ошибка, если транзакция не найдена.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransaction(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Получает все транзакции.
     *
     * @return Список всех транзакций.
     */
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getTransactions();
        return ResponseEntity.ok(transactions);
    }

    /**
     * Обновляет существующую транзакцию.
     *
     * @param id             Идентификатор транзакции.
     * @param transactionDto Данные для обновления транзакции.
     * @return Обновленная транзакция или 404 ошибка, если транзакция не найдена.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody TransactionDto transactionDto) {
        try {
            Transaction updatedTransaction = transactionService.updateTransaction(id, transactionDto);
            return ResponseEntity.ok(updatedTransaction);
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Удаляет транзакцию по её идентификатору.
     *
     * @param id Идентификатор транзакции.
     * @return 200 OK, если транзакция успешно удалена, или 404 ошибка, если транзакция не найдена.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        try {
            transactionService.deleteTransaction(id);
            return ResponseEntity.ok().build();
        } catch (TransactionNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}