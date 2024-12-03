package ru.t1.java.demo.service.impl;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.model.enums.AccountStatus;
import ru.t1.java.demo.model.enums.ClientStatus;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.repository.ClientRepository;
import ru.t1.java.demo.service.MetricsService;

@Service
@RequiredArgsConstructor
public class MetricsServiceImpl implements MetricsService {
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final MeterRegistry meterRegistry;


    @Override
    @Scheduled(fixedRateString = "${t1.time.fixed_rate}")
    public void updateMetrics() {
        long blackListedClientCount = clientRepository.countClientsByStatus(ClientStatus.BLACKLISTED);
        long arrestedAccountCount = accountRepository.countAccountByAccountStatus(AccountStatus.ARRESTED);

        meterRegistry.gauge("blacklisted_clients", blackListedClientCount);
        meterRegistry.gauge("arrested_accounts", arrestedAccountCount);

        System.out.println("blacklisted_clients: " + blackListedClientCount);
        System.out.println("arrested_accounts: " + arrestedAccountCount);
    }
}
