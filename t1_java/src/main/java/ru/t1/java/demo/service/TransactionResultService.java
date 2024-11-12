package ru.t1.java.demo.service;

import ru.t1.java.demo.dto.TransactionResultMessageDto;

public interface TransactionResultService {
    void processTransactionResult(TransactionResultMessageDto message);
}
