package ru.t1.java.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.exception.ClientException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.repository.ClientRepository;

/**
 * Маппер для преобразования между DTO и сущностью Account.
 * Предоставляет методы для конвертации AccountDto в Account и обратно.
 *
 * @author ivanogor
 * @version 1.0
 * @since 7.11.2024
 */
@Component
@RequiredArgsConstructor
public class AccountMapper {

    /**
     * Репозиторий для работы с сущностью Client.
     */
    private final ClientRepository clientRepository;

    /**
     * Преобразует DTO в сущность Account.
     *
     * @param dto DTO с данными для создания сущности Account.
     * @return Сущность Account.
     * @throws ClientException если клиент с указанным ID не найден.
     */
    public Account toEntity(AccountDto dto) {
        Account account = new Account();
        account.setAccountType(dto.getAccountType());
        account.setBalance(dto.getBalance());

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ClientException("Client not found with id: " + dto.getClientId()));
        account.setClient(client);

        return account;
    }

    /**
     * Преобразует сущность Account в DTO.
     *
     * @param account Сущность Account.
     * @return DTO с данными сущности Account.
     */
    public AccountDto toDto(Account account) {
        Client client = account.getClient();
        Long clientId = client != null ? client.getId() : null;

        return AccountDto.builder()
                .id(account.getId())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .clientId(clientId)
                .accountId(account.getAccountId())
                .accountType(account.getAccountType())
                .frozenAmount(account.getFrozenAmount())
                .build();
    }
}