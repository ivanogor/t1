package ru.t1.java.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;
import ru.t1.java.demo.service.DataSourceErrorLogService;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataSourceErrorLogServiceImpl implements DataSourceErrorLogService {

    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;


    @Override
    public DataSourceErrorLog saveDataSourceErrorLog(DataSourceErrorLog dataSourceErrorLog) {
        return dataSourceErrorLogRepository.save(dataSourceErrorLog);
    }
}
