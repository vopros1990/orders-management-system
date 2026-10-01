package com.example.orders_api_gateway.event;

import com.example.dto.*;
import com.example.orders_api_gateway.configuration.AppProducerProperties;
import com.example.orders_api_gateway.dto.OrderMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderConfirmedEvent> template;

    private final AppProducerProperties producerProperties;

    public void send(OrderMetadata order) {

        OrderConfirmedEvent event = OrderConfirmedEvent.builder()
                .orderId(order.orderId())
                .customerName(order.customerName())
                .deliveryMethod(DeliveryMethod.valueOf(order.deliveryMethod().toString()))
                .items(order.items())
                .build();

        String uuid = UUID.randomUUID().toString();

        template.send(producerProperties.topicName(), uuid, event).whenComplete((result, exception) -> {

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
