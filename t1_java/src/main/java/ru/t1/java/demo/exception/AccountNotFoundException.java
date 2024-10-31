package ru.t1.java.demo.exception;

/**
 * Исключение, выбрасываемое, когда банковский счет не найден.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
public class AccountNotFoundException extends RuntimeException {

    /**
     * Создает новое исключение с указанным идентификатором счета.
     *
     * @param id Идентификатор счета, который не был найден.
     */
    public AccountNotFoundException(Long id) {
        super("Account with id " + id + " not found");
    }
}