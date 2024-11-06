package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final long executionTimeThreshold = 1000;

    @Around("@annotation(Metric)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;

        if(executionTime > executionTimeThreshold) {
            String message = "Error type: METRICS, Method: " + joinPoint.getSignature().getName()
                    + ", Execution time: " + executionTime + "ms, Parameters: " + Arrays.toString(joinPoint.getArgs());

            String key = UUID.randomUUID().toString();
            kafkaTemplate.send("t1_demo_metrics", key, message);
        }

        return proceed;
    }
}
