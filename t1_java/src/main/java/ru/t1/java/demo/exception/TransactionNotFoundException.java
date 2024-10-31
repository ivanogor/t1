package ru.t1.java.demo.exception;

public class TransactionNotFoundException extends RuntimeException{
    public TransactionNotFoundException(Long id) {
        super("Account with id " + id + " not found");
    }
}
