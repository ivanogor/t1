package ru.t1.java.demo.util;

import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.model.Account;

public class AccountMapper {
    public static Account toEntity(AccountDto account) {
        return Account.builder()
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .build();
    }

    public static AccountDto toDto(Account account) {
        return AccountDto.builder()
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .build();
    }
}
