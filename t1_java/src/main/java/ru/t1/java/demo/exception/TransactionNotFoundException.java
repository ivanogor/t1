package ru.t1.java.demo.exception;

/**
 * Исключение, выбрасываемое, когда транзакция не найдена.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
public class TransactionNotFoundException extends RuntimeException {

    /**
     * Создает новое исключение с указанным идентификатором транзакции.
     *
     * @param id Идентификатор транзакции, которая не была найдена.
     */
    public TransactionNotFoundException(Long id) {
        super("Transaction with id " + id + " not found");
    }
}