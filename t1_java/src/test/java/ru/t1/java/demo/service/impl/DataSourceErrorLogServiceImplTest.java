package ru.t1.java.demo.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DataSourceErrorLogServiceImplTest {

    @Mock
    private DataSourceErrorLogRepository dataSourceErrorLogRepository;

    @InjectMocks
    private DataSourceErrorLogServiceImpl dataSourceErrorLogService;

    private DataSourceErrorLog dataSourceErrorLog;

    @BeforeEach
    public void setUp() {
        dataSourceErrorLog = new DataSourceErrorLog();
        dataSourceErrorLog.setMessage("Test error message");
    }

    @Test
    public void testSaveDataSourceErrorLog() {
        // Подготовка данных
        when(dataSourceErrorLogRepository.save(any(DataSourceErrorLog.class))).thenReturn(dataSourceErrorLog);

        // Вызов тестируемого метода
        DataSourceErrorLog savedLog = dataSourceErrorLogService.saveDataSourceErrorLog(dataSourceErrorLog);

        // Проверка результатов
        verify(dataSourceErrorLogRepository).save(dataSourceErrorLog);
        assertEquals(dataSourceErrorLog.getMessage(), savedLog.getMessage());
    }
}