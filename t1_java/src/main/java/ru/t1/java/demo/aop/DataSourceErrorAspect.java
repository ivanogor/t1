package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.service.DataSourceErrorLogService;

import java.util.Arrays;

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
        errorLogService.saveDataSourceErrorLog(errorLog);
    }
}