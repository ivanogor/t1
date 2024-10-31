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

@Aspect
@Component
@RequiredArgsConstructor
public class DataSourceErrorAspect {
    private final DataSourceErrorLogService errorLogService;

    @Pointcut("within(ru.t1.java.demo.service.impl.*)")
    public void loggingMethods() {

    }

    @AfterThrowing(pointcut = "loggingMethods()", throwing = "e")
    public void logDataSourceError(JoinPoint joinPoint, Throwable e) {
        System.out.println("ОШИБКАОШИБКАОШИБКАОШИБКАОШИБКАОШИБКАОШИБКАОШИБКА!!");
        DataSourceErrorLog errorLog = DataSourceErrorLog.builder()
                .stackTrace(Arrays.toString(e.getStackTrace()))
                .message(e.getMessage())
                .methodSignature(joinPoint.getSignature().toLongString())
                .build();
        errorLogService.saveDataSourceErrorLog(errorLog);
    }
}
