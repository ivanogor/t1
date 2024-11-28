package ru.t1.java.demo.service.impl;

import io.micrometer.core.instrument.Metrics;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.model.enums.AccountStatus;
import ru.t1.java.demo.model.enums.ClientStatus;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class MetricsServiceImpl {
    private ClientRepository clientRepository;

    private AccountRepository accountRepository;


    @Scheduled(fixedRateString = "${t1.time.fixed_rate}")
    public void updateMetrics(){
        long blackListedClientCount = clientRepository.countClientsByStatus(ClientStatus.BLACKLISTED);

        long arrestedAccountCount = accountRepository.countAccountByAccountStatus(AccountStatus.ARRESTED);

        Metrics.gauge("blacklisted_clients", blackListedClientCount);
        Metrics.gauge("arrested_accounts", arrestedAccountCount);
    }

    //TODO: ПРОЕКТ НЕ ЗАКОНЧЕН!!
}
