package ru.t1.java.demo.exception;

public class AccountNotFoundException extends RuntimeException{

    public AccountNotFoundException(Long id) {
        super("Account with id " + id + " not found");
    }

}
