package com.ivanogor.t1metricsstarter.repository;


import com.ivanogor.t1metricsstarter.model.DataSourceErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью DataSourceErrorLog.
 * Предоставляет базовые операции CRUD (создание, чтение, обновление, удаление) для журнала ошибок источника данных.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
@Repository
public interface DataSourceErrorLogRepository extends JpaRepository<DataSourceErrorLog, Long> {
}
