package com.example.orders_api_gateway.configuration;

import com.example.dto.OrderConfirmedEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableConfigurationProperties(AppProducerProperties.class)
@RequiredArgsConstructor
public class KafkaConfig {

    private final AppProducerProperties producerProperties;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public NewTopic ordersNewTopic() {

        return TopicBuilder.name(producerProperties.topicName())
                .partitions(producerProperties.partitions())
                .replicas(producerProperties.replicas())
                .build();
    }

    @Bean
    public ProducerFactory<String, OrderConfirmedEvent> producerFactory() {

        Map<String, Object> properties = new HashMap<>();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(properties);

    }

    @Bean
    public KafkaTemplate<String, OrderConfirmedEvent> kafkaTemplate () {

        return new KafkaTemplate<>(producerFactory());

    }

}
