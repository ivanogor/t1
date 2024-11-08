package ru.t1.java.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.exception.ClientException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.repository.ClientRepository;

@Component
@RequiredArgsConstructor
public class AccountMapper {
    private final ClientRepository clientRepository;


    public Account toEntity(AccountDto dto) {
        Account account = new Account();
        account.setAccountType(dto.getAccountType());
        account.setBalance(dto.getBalance());

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ClientException("Client not found with id: " + dto.getClientId()));
        account.setClient(client);

        return account;
    }

    public AccountDto toDto(Account account) {
        Client client = account.getClient();
        Long clientId = client != null ? client.getId() : null;

        return AccountDto.builder()
                .id(account.getId())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .clientId(clientId)
                .build();
    }
}
