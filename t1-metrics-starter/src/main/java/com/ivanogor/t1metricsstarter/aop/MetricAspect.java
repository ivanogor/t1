package com.ivanogor.t1metricsstarter.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

/**
 * Аспект для сбора и логирования метрик выполнения методов, помеченных аннотацией {@link Metric}.
 * Если время выполнения метода превышает заданный порог, метрика отправляется в Kafka.
 *
 * @author ivanogor
 * @version 1.0
 * @since 7.11.2024
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class MetricAspect {

    /**
     * Шаблон Kafka для отправки сообщений.
     */
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Порог времени выполнения метода, после которого метрика отправляется в Kafka.
     */
    @Value("t1.time.threshold")
    private static long executionTimeThreshold;

    /**
     * Имя топика Kafka, в который отправляются метрики.
     */
    @Value("${t1.kafka.topic.metrics}")
    private static String TOPIC_NAME;

    /**
     * Тип сообщения для Kafka, указывающий на то, что это метрика.
     */
    private static final String MESSAGE_TYPE_METRICS = "METRICS";

    /**
     * Совет, который выполняется вокруг методов, помеченных аннотацией {@link Metric}.
     * Если время выполнения метода превышает заданный порог, метрика отправляется в Kafka.
     *
     * @param joinPoint Точка соединения, представляющая выполнение метода.
     * @return Результат выполнения метода.
     * @throws Throwable Исключение, которое может быть выброшено методом.
     */
    @Around("@annotation(Metric)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;

        if (executionTime > executionTimeThreshold) {
            Message<String> message = createKafkaMessage(joinPoint, executionTime);
            try {
                kafkaTemplate.send(message);
                log.info("Successfully sent metrics message to Kafka: {}", message);
            } catch (Exception e) {
                log.error("Failed to send metrics message to Kafka", e);
            }
        }

        return proceed;
    }

    /**
     * Создает сообщение Kafka с заголовками и телом, содержащим информацию о метрике.
     *
     * @param joinPoint     Точка соединения, представляющая выполнение метода.
     * @param executionTime Время выполнения метода в миллисекундах.
     * @return Сообщение Kafka.
     */
    private Message<String> createKafkaMessage(ProceedingJoinPoint joinPoint, long executionTime) {
        String message = "Method: " + joinPoint.getSignature().getName()
                + ", Execution time: " + executionTime + "ms, Parameters: " + Arrays.toString(joinPoint.getArgs());
        String key = UUID.randomUUID().toString();

        return MessageBuilder.withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME)
                .setHeader(KafkaHeaders.KEY, key)
                .setHeader("message_type", MESSAGE_TYPE_METRICS)
                .build();
    }
}
