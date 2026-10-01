package com.example.payment.configuration;

import com.example.dto.OrderConfirmedEvent;
import com.example.dto.OrderPayedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableConfigurationProperties({
        AppConsumerProperties.class,
        AppProducerProperties.class
})
@RequiredArgsConstructor
@Slf4j
public class KafkaConfig {

    private final AppConsumerProperties consumerProperties;

    private final AppProducerProperties producerProperties;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public NewTopic payedOrdersTopic() {

        return TopicBuilder.name(producerProperties.topicName())
                .partitions(producerProperties.partitions())
                .replicas(producerProperties.replicas())
                .build();
    }

    @Bean
    public ProducerFactory<String, OrderPayedEvent> producerFactory() {

        Map<String, Object> properties = new HashMap<>();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(properties);

    }

    @Bean
    public KafkaTemplate<String, OrderPayedEvent> kafkaTemplate () {

        return new KafkaTemplate<>(producerFactory());

    }

    @Bean
    public ConsumerFactory<String, OrderConfirmedEvent> consumerFactory() {

        Map<String, Object> props = new HashMap<>();

        JacksonJsonDeserializer<OrderConfirmedEvent> valueDeserializer = new JacksonJsonDeserializer<>(OrderConfirmedEvent.class);

        valueDeserializer.addTrustedPackages("*");

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer.getClass());

        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.groupId());

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), valueDeserializer);

    }

    @Bean
    public DefaultErrorHandler errorHandler() {

        BackOff fixedBackoff = new FixedBackOff(
                consumerProperties.backoffMs(),
                consumerProperties.maxRetryAttempts()
        );

        DefaultErrorHandler handler = new DefaultErrorHandler((
                (record, e) ->
                        log.error("Error occurred while consuming message. Topic: {}, partition: {}, offset: {}, key: {}",
                                record.topic(),
                                record.partition(),
                                record.offset(),
                                record.key(),
                                e
                        )
        ), fixedBackoff);

        handler.addNotRetryableExceptions(NullPointerException.class);

        return handler;

    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderConfirmedEvent> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, OrderConfirmedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        factory.setConcurrency(consumerProperties.concurrency());

        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        factory.setCommonErrorHandler(errorHandler());

        return factory;

    }

}
