package ru.t1.java.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.exception.AccountNotFoundException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.AccountRepository;

@RequiredArgsConstructor
@Component
public class TransactionMapper {
    private final AccountRepository accountRepository;

    public Transaction toEntity(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDto.getAmount());
        long id = transactionDto.getAccountId();
        Account account = accountRepository.findById(transactionDto.getAccountId())
                .orElseThrow(() -> new AccountNotFoundException(id));

        transaction.setAccount(account);
        return transaction;
    }

    public TransactionDto toDto(Transaction transaction) {
        Account account = transaction.getAccount();
        Long accountId = account != null ? account.getId() : null;

        return TransactionDto.builder()
                .id(transaction.getId())
                .accountId(accountId)
                .amount(transaction.getAmount())
                .transactionStatus(transaction.getTransactionStatus())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }
}
