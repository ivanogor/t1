package ru.t1.java.demo.exception;

/**
 * Исключение, которое выбрасывается, когда статус счета не является "открытым".
 * Это может произойти, например, если счет закрыт или заблокирован.
 *
 * @author ivanogor
 * @version 1.0
 * @since 7.11.2024
 */
public class AccountStatusIsNotOpenedException extends RuntimeException {

    /**
     * Создает новое исключение с сообщением "Account status is not opened".
     */
    public AccountStatusIsNotOpenedException() {
        super("Account status is not opened");
    }
}