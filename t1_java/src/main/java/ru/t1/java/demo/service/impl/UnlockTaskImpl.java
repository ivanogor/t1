package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.dto.ClientDto;
import ru.t1.java.demo.dto.UnblockDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.enums.AccountStatus;
import ru.t1.java.demo.model.enums.ClientStatus;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.ClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnlockTaskImpl {
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${t1.limit.account}")
    private int accountLimit;

    @Value("${t1.limit.client}")
    private int clientLimit;

    @Value("${t1.client.service3.url}")
    private String service3Url;

    @Scheduled(fixedRateString = "${t1.time.fixed_rate}")
    public void unlockClients() {
        List<Client> clients = clientRepository.findTopNBlockedClients(clientLimit);
        clients.forEach(client -> {
            ClientDto clientDto = ClientDto.builder()
                    .clientId(client.getClientId().toString())
                    .build();
            Mono<UnblockDto> responseDtoMono = webClientBuilder.build()
                    .post()
                    .uri(service3Url + "/api/unlock/client")
                    .bodyValue(clientDto)
                    .retrieve()
                    .bodyToMono(UnblockDto.class);

            responseDtoMono.subscribe(responseDto -> {
                if (responseDto.isUnblocked()) {
                    client.setStatus(ClientStatus.NORMAL);
                    clientRepository.save(client);
                }
            });
        });
    }

    @Scheduled(fixedRateString = "${t1.time.fixed_rate}")
    public void unlockAccounts() {
        List<Account> accounts = accountRepository.findTopMBlockedAccounts(accountLimit);
        accounts.forEach(account -> {
            AccountDto accountDto = AccountDto.builder()
                    .accountId(account.getAccountId())
                    .build();
            Mono<UnblockDto> responseDtoMono = webClientBuilder.build()
                    .post()
                    .uri(service3Url + "/api/unlock/account")
                    .bodyValue(accountDto)
                    .retrieve()
                    .bodyToMono(UnblockDto.class);

            responseDtoMono.subscribe(responseDto -> {
                if (responseDto.isUnblocked()) {
                    account.setAccountStatus(AccountStatus.OPEN);
                    accountRepository.save(account);
                }
            });
        });
    }
}
