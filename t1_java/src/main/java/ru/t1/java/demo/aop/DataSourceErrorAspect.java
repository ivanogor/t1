package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.service.DataSourceErrorLogService;

import java.util.Arrays;
import java.util.UUID;

/**
 * Аспект для логирования ошибок, связанных с источниками данных.
 * Этот аспект перехватывает исключения, выброшенные в пакете 'ru.t1.java.demo',
 * и логирует их с использованием сервиса {@link DataSourceErrorLogService}.
 *
 * @author ivanogor
 * @version 1.0
 * @since 30.10.2024
 */
@Aspect
@Component
@RequiredArgsConstructor
public class DataSourceErrorAspect {

    private final KafkaTemplate<String, String> kafkaTemplate;
    /**
     * Сервис, отвечающий за сохранение логов ошибок, связанных с источниками данных.
     */
    private final DataSourceErrorLogService errorLogService;

    /**
     * Точка среза, соответствующая всем методам в пакете 'ru.t1.java.demo'.
     */
    @Pointcut("within(ru.t1.java.demo.*)")
    public void loggingMethods() {
    }

    /**
     * Совет, который выполняется после того, как метод в пакете 'ru.t1.java.demo' выбрасывает исключение.
     * Он логирует детали исключения с использованием {@link DataSourceErrorLogService}.
     *
     * @param joinPoint Точка соединения, представляющая выполнение метода.
     * @param e         Исключение, которое было выброшено.
     */
    @AfterThrowing(pointcut = "loggingMethods()", throwing = "e")
    public void logDataSourceError(JoinPoint joinPoint, Throwable e) {
        DataSourceErrorLog errorLog = DataSourceErrorLog.builder()
                .stackTrace(Arrays.toString(e.getStackTrace()))
                .message(e.getMessage())
                .methodSignature(joinPoint.getSignature().toLongString())
                .build();

        String errorMessage = "Error type: DATA_SOURCE, Message: " + errorLog.toString();
        String key = UUID.randomUUID().toString();

        try {
            kafkaTemplate.send("t1_demo_metrics", key, errorMessage);
        } catch (Exception ex){
            saveErrorLogToDataBase(errorLog);
        }
    }

    private void saveErrorLogToDataBase(DataSourceErrorLog errorLog) {
        errorLogService.saveDataSourceErrorLog(errorLog);
    }
}