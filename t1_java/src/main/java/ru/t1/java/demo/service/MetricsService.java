package ru.t1.java.demo.service;

import org.springframework.scheduling.annotation.Scheduled;

public interface MetricsService {
    @Scheduled(fixedRateString = "${t1.time.fixed_rate}")
    void updateMetrics();
}
