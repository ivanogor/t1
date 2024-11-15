package com.demo.service.kafka.consumer;

import com.demo.service.dto.TransactionAcceptedMessageDto;
import com.demo.service.dto.TransactionResultMessageDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация для Kafka Consumer.
 * Настраивает фабрики потребителей для обработки сообщений из Kafka.
 *
 * @author ivanogor
 * @version 1.0
 * @since 7.11.2024
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    /**
     * Адреса серверов Kafka, к которым подключается потребитель.
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Идентификатор группы потребителя Kafka.
     */
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    /**
     * Тайм-аут сессии потребителя Kafka.
     */
    @Value("${t1.kafka.consumer.properties.session.timeout.ms}")
    private String sessionTimeout;

    /**
     * Максимальный размер данных, которые могут быть получены от одного раздела.
     */
    @Value("${t1.kafka.consumer.properties.max.partition.fetch.bytes}")
    private String maxPartitionFetchBytes;

    /**
     * Максимальное количество записей, которые могут быть получены за один вызов poll().
     */
    @Value("${t1.kafka.consumer.properties.max.poll.records}")
    private String maxPollRecords;

    /**
     * Максимальный интервал времени между вызовами poll().
     */
    @Value("${t1.kafka.consumer.properties.max.poll.interval.ms}")
    private String maxPollIntervalMs;

    /**
     * Создает конфигурацию потребителя Kafka.
     *
     * @return Конфигурация потребителя Kafka.
     */
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);

        return props;
    }

    /**
     * Создает фабрику потребителей Kafka для указанного класса.
     *
     * @param clazz Класс, для которого создается фабрика потребителей.
     * @param <T>   Тип данных, которые будут десериализованы из Kafka.
     * @return Фабрика потребителей Kafka.
     */
    @Bean
    public <T> ConsumerFactory<String, T> consumerFactory(Class<T> clazz) {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new JsonDeserializer<>(clazz));
    }

    /**
     * Создает фабрику слушателей Kafka для указанной фабрики потребителей.
     *
     * @param consumerFactory Фабрика потребителей Kafka.
     * @param <T>             Тип данных, которые будут десериализованы из Kafka.
     * @return Фабрика слушателей Kafka.
     */
    @Bean
    public <T> ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory(ConsumerFactory<String, T> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    /**
     * Создает фабрику потребителей Kafka для сообщений о результатах транзакций.
     *
     * @return Фабрика потребителей Kafka для сообщений о результатах транзакций.
     */
    @Bean
    public ConsumerFactory<String, TransactionResultMessageDto> kafkaTransactionResultMessageDtoConsumerFactory() {
        return consumerFactory(TransactionResultMessageDto.class);
    }

    /**
     * Создает фабрику слушателей Kafka для сообщений о результатах транзакций.
     *
     * @return Фабрика слушателей Kafka для сообщений о результатах транзакций.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionResultMessageDto> kafkaTransactionResultMessageDtoListenerContainerFactory() {
        return kafkaListenerContainerFactory(kafkaTransactionResultMessageDtoConsumerFactory());
    }

    /**
     * Создает фабрику потребителей Kafka для сообщений о принятых транзакциях.
     *
     * @return Фабрика потребителей Kafka для сообщений о принятых транзакциях.
     */
    @Bean
    public ConsumerFactory<String, TransactionAcceptedMessageDto> kafkaTransactionAcceptedMessageDtoConsumerFactory() {
        return consumerFactory(TransactionAcceptedMessageDto.class);
    }

    /**
     * Создает фабрику слушателей Kafka для сообщений о принятых транзакциях.
     *
     * @return Фабрика слушателей Kafka для сообщений о принятых транзакциях.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionAcceptedMessageDto> kafkaTransactionAcceptedMessageDtoListenerContainerFactory() {
        return kafkaListenerContainerFactory(kafkaTransactionAcceptedMessageDtoConsumerFactory());
    }
}