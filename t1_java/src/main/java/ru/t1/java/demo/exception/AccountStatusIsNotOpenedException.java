package ru.t1.java.demo.exception;

public class AccountStatusIsNotOpenedException extends RuntimeException{
    public AccountStatusIsNotOpenedException() {
        super("Account status is not opened");
    }
}
