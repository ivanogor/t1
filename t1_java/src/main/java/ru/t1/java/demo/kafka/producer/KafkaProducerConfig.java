package ru.t1.java.demo.kafka.producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.dto.TransactionAcceptedMessageDto;
import ru.t1.java.demo.dto.TransactionDto;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация для Kafka Producer.
 * Настраивает фабрики продюсеров для отправки сообщений в Kafka.
 *
 * @author ivanogor
 * @version 1.0
 * @since 7.11.2024
 */
@Configuration
public class KafkaProducerConfig {

    /**
     * Адреса серверов Kafka, к которым подключается продюсер.
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Создает фабрику продюсеров Kafka для указанного класса.
     *
     * @param <T> Тип данных, которые будут сериализованы в Kafka.
     * @return Фабрика продюсеров Kafka.
     */
    @Bean
    public <T> ProducerFactory<String, T> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Создает KafkaTemplate для отправки сообщений о счетах.
     *
     * @return KafkaTemplate для отправки сообщений о счетах.
     */
    @Bean
    public KafkaTemplate<String, AccountDto> accountDtoKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Создает KafkaTemplate для отправки метрик.
     *
     * @return KafkaTemplate для отправки метрик.
     */
    @Bean
    public KafkaTemplate<String, String> metricsKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Создает KafkaTemplate для отправки сообщений о транзакциях.
     *
     * @return KafkaTemplate для отправки сообщений о транзакциях.
     */
    @Bean
    public KafkaTemplate<String, TransactionDto> transactionKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Создает KafkaTemplate для отправки сообщений о принятых транзакциях.
     *
     * @return KafkaTemplate для отправки сообщений о принятых транзакциях.
     */
    @Bean
    public KafkaTemplate<String, TransactionAcceptedMessageDto> transactionAcceptedMessageKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}