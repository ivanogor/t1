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

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${t1.kafka.consumer.properties.session.timeout.ms}")
    private String sessionTimeout;

    @Value("${t1.kafka.consumer.properties.max.partition.fetch.bytes}")
    private String maxPartitionFetchBytes;

    @Value("${t1.kafka.consumer.properties.max.poll.records}")
    private String maxPollRecords;

    @Value("${t1.kafka.consumer.properties.max.poll.interval.ms}")
    private String maxPollIntervalMs;

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

    @Bean
    public <T> ConsumerFactory<String, T> consumerFactory(Class<T> clazz) {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new JsonDeserializer<>(clazz));
    }

    @Bean
    public <T> ConcurrentKafkaListenerContainerFactory<String, T> kafkaListenerContainerFactory(ConsumerFactory<String, T> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, AccountDto> consumerAccountFactory() {
        return consumerFactory(AccountDto.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountDto> kafkaAccountListenerContainerFactory(
            ConsumerFactory<String, AccountDto> consumerAccountFactory) {
        return kafkaListenerContainerFactory(consumerAccountFactory);
    }

    @Bean
    public ConsumerFactory<String, TransactionDto> consumerTransactionalFactory() {
        return consumerFactory(TransactionDto.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionDto> kafkaTransactionalListenerContainerFactory(
            ConsumerFactory<String, TransactionDto> consumerTransactionalFactory) {
        return kafkaListenerContainerFactory(consumerTransactionalFactory);
    }

    @Bean
    public ConsumerFactory<String, TransactionResultMessageDto> kafkaTransactionResultMessageDtoConsumerFactory() {
        return consumerFactory(TransactionResultMessageDto.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionResultMessageDto> kafkaTransactionResultMessageDtoListenerContainerFactory(
            ConsumerFactory<String, TransactionResultMessageDto> kafkaTransactionResultMessageDtoConsumerFactory) {
        return kafkaListenerContainerFactory(kafkaTransactionResultMessageDtoConsumerFactory);
    }

    @Bean
    public ConsumerFactory<String, TransactionAcceptedMessageDto> kafkaTransactionAcceptedMessageDtoConsumerFactory() {
        return consumerFactory(TransactionAcceptedMessageDto.class);
    }
}