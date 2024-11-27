package com.ivanogor.t1metricsstarter.service.impl;

import com.ivanogor.t1metricsstarter.model.DataSourceErrorLog;
import com.ivanogor.t1metricsstarter.repository.DataSourceErrorLogRepository;
import com.ivanogor.t1metricsstarter.service.DataSourceErrorLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса для работы с логами ошибок источников данных.
 * Предоставляет метод для сохранения логов ошибок в базу данных.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DataSourceErrorLogServiceImpl implements DataSourceErrorLogService {

    /**
     * Репозиторий для работы с сущностью DataSourceErrorLog.
     */
    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;

    @Override
    public DataSourceErrorLog saveDataSourceErrorLog(DataSourceErrorLog dataSourceErrorLog) {
        log.info("Сохранение лога ошибки источника данных: начато");
        DataSourceErrorLog savedLog = dataSourceErrorLogRepository.save(dataSourceErrorLog);
        log.info("Сохранение лога ошибки источника данных: завершено, ID: {}", savedLog.getId());
        return savedLog;
    }
}
