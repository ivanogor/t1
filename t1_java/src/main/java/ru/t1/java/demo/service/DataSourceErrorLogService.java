package ru.t1.java.demo.service;

import ru.t1.java.demo.model.DataSourceErrorLog;

/**
 * Интерфейс сервиса для работы с сущностью DataSourceErrorLog.
 * Предоставляет методы для сохранения, получения, обновления и удаления журнала ошибок источника данных.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
public interface DataSourceErrorLogService {

    /**
     * Сохраняет новый журнал ошибок источника данных в системе.
     *
     * @param dataSourceErrorLog журнал ошибок источника данных для сохранения
     * @return сохраненный журнал ошибок источника данных
     */
    DataSourceErrorLog saveDataSourceErrorLog(DataSourceErrorLog dataSourceErrorLog);
}