package ru.t1.java.demo.util;

import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.model.Transaction;

public class TransactionMapper {
    public static Transaction toEntity(TransactionDto transactionDto) {
        return Transaction.builder()
                .account(transactionDto.getAccount())
                .amount(transactionDto.getAmount())
                .build();
    }

    public static TransactionDto toDto(Transaction transaction) {
        return TransactionDto.builder()
                .account(transaction.getAccount())
                .amount(transaction.getAmount())
                .build();
    }
}
