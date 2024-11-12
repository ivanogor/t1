package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
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
 * @version 2.0
 * @since 7.11.2024
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSourceErrorAspect {

    /**
     * Шаблон Kafka для отправки сообщений.
     */
    private final KafkaTemplate<String, DataSourceErrorLog> kafkaTemplate;

    /**
     * Сервис, отвечающий за сохранение логов ошибок, связанных с источниками данных.
     */
    private final DataSourceErrorLogService errorLogService;

    /**
     * Имя топика Kafka, в который отправляются логи ошибок.
     */
    @Value("${t1.kafka.topic.metrics}")
    private static String TOPIC_NAME;

    /**
     * Тип сообщения для Kafka, указывающий на то, что это метрика источника данных.
     */
    private static final String MESSAGE_TYPE_METRICS = "DATA_SOURCE";

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
        DataSourceErrorLog errorLog = createErrorLog(joinPoint, e);
        Message<DataSourceErrorLog> message = createKafkaMessage(errorLog);

        try {
            kafkaTemplate.send(message);
            log.info("Successfully sent error log to Kafka: {}", errorLog);
        } catch (Exception ex) {
            log.error("Failed to send message to Kafka", ex);
            saveErrorLogToDataBase(errorLog);
        }
    }

    /**
     * Создает сообщение Kafka с заголовками и телом, содержащим информацию об ошибке.
     *
     * @param errorLog Объект, содержащий информацию об ошибке.
     * @return Сообщение Kafka.
     */
    private Message<DataSourceErrorLog> createKafkaMessage(DataSourceErrorLog errorLog) {
        String key = UUID.randomUUID().toString();

        return MessageBuilder
                .withPayload(errorLog)
                .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME)
                .setHeader(KafkaHeaders.KEY, key)
                .setHeader("message_type", MESSAGE_TYPE_METRICS)
                .build();
    }

    /**
     * Сохраняет лог ошибки в базе данных.
     *
     * @param errorLog Объект, содержащий информацию об ошибке.
     */
    private void saveErrorLogToDataBase(DataSourceErrorLog errorLog) {
        errorLogService.saveDataSourceErrorLog(errorLog);
        log.info("Error log saved to database: {}", errorLog);
    }

    /**
     * Создает объект лога ошибки на основе перехваченного исключения.
     *
     * @param joinPoint Точка соединения, представляющая выполнение метода.
     * @param e         Исключение, которое было выброшено.
     * @return Объект лога ошибки.
     */
    private DataSourceErrorLog createErrorLog(JoinPoint joinPoint, Throwable e) {
        return DataSourceErrorLog.builder()
                .stackTrace(Arrays.toString(e.getStackTrace()))
                .message(e.getMessage())
                .methodSignature(joinPoint.getSignature().toLongString())
                .build();
    }
}