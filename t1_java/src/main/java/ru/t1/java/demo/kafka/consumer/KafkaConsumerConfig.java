package ru.t1.java.demo.kafka.consumer;

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
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.dto.TransactionAcceptedMessageDto;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.dto.TransactionResultMessageDto;

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
     * Создает конфигурацию потребителя Kafka.
     *
     * @return Конфигурация потребителя Kafka.
     */
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
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
     * Создает фабрику потребителей Kafka для обработки сообщений типа {@link AccountDto}.
     *
     * @return Фабрика потребителей Kafka для {@link AccountDto}.
     */
    @Bean
    public ConsumerFactory<String, AccountDto> consumerAccountFactory() {
        return consumerFactory(AccountDto.class);
    }

    /**
     * Создает фабрику слушателей Kafka для обработки сообщений типа {@link AccountDto}.
     *
     * @param consumerAccountFactory Фабрика потребителей Kafka для {@link AccountDto}.
     * @return Фабрика слушателей Kafka для {@link AccountDto}.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountDto> kafkaAccountListenerContainerFactory(
            ConsumerFactory<String, AccountDto> consumerAccountFactory) {
        return kafkaListenerContainerFactory(consumerAccountFactory);
    }

    /**
     * Создает фабрику потребителей Kafka для обработки сообщений типа {@link TransactionDto}.
     *
     * @return Фабрика потребителей Kafka для {@link TransactionDto}.
     */
    @Bean
    public ConsumerFactory<String, TransactionDto> consumerTransactionalFactory() {
        return consumerFactory(TransactionDto.class);
    }

    /**
     * Создает фабрику слушателей Kafka для обработки сообщений типа {@link TransactionDto}.
     *
     * @param consumerTransactionalFactory Фабрика потребителей Kafka для {@link TransactionDto}.
     * @return Фабрика слушателей Kafka для {@link TransactionDto}.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionDto> kafkaTransactionalListenerContainerFactory(
            ConsumerFactory<String, TransactionDto> consumerTransactionalFactory) {
        return kafkaListenerContainerFactory(consumerTransactionalFactory);
    }

    /**
     * Создает фабрику потребителей Kafka для обработки сообщений типа {@link TransactionResultMessageDto}.
     *
     * @return Фабрика потребителей Kafka для {@link TransactionResultMessageDto}.
     */
    @Bean
    public ConsumerFactory<String, TransactionResultMessageDto> kafkaTransactionResultMessageDtoConsumerFactory() {
        return consumerFactory(TransactionResultMessageDto.class);
    }

    /**
     * Создает фабрику слушателей Kafka для обработки сообщений типа {@link TransactionResultMessageDto}.
     *
     * @param kafkaTransactionResultMessageDtoConsumerFactory Фабрика потребителей Kafka для {@link TransactionResultMessageDto}.
     * @return Фабрика слушателей Kafka для {@link TransactionResultMessageDto}.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionResultMessageDto> kafkaTransactionResultMessageDtoListenerContainerFactory(
            ConsumerFactory<String, TransactionResultMessageDto> kafkaTransactionResultMessageDtoConsumerFactory) {
        return kafkaListenerContainerFactory(kafkaTransactionResultMessageDtoConsumerFactory);
    }

    /**
     * Создает фабрику потребителей Kafka для обработки сообщений типа {@link TransactionAcceptedMessageDto}.
     *
     * @return Фабрика потребителей Kafka для {@link TransactionAcceptedMessageDto}.
     */
    @Bean
    public ConsumerFactory<String, TransactionAcceptedMessageDto> kafkaTransactionAcceptedMessageDtoConsumerFactory() {
        return consumerFactory(TransactionAcceptedMessageDto.class);
    }
}