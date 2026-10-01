package com.example.payment.event;


import com.example.payment.configuration.AppProducerProperties;
import com.example.payment.dto.OrderMetadata;
import com.example.dto.OrderPayedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderPayedEvent> template;

    private final AppProducerProperties producerProperties;

    public void send(OrderMetadata metadata) {

        OrderPayedEvent event = OrderPayedEvent.builder()
                .orderId(metadata.orderId())
                .customerName(metadata.customerName())
                .deliveryMethod(metadata.deliveryMethod())
                .items(metadata.items())
                .totalAmount(metadata.totalAmount())
                .build();

        String uuid = UUID.randomUUID().toString();

        template.send(producerProperties.topicName(), uuid, event)
                .whenComplete((result, exception) -> {

                    if (exception != null) {

                        log.error("Failed to send event", exception);

                    } else {

                        log.info("Sent event. Topic: {}, partition: {}, offset{}, key: {}, order ID: {}.",
                                producerProperties.topicName(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset(),
                                uuid,
                                event.orderId()
                        );

                    }

                });

    }

}
