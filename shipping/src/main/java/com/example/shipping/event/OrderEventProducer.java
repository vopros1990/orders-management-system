package com.example.shipping.event;

import com.example.shipping.configuration.AppProducerProperties;
import com.example.shipping.dto.OrderMetadata;
import com.example.dto.OrderSentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderSentEvent> template;

    private final AppProducerProperties producerProperties;

    public void send(OrderMetadata metadata) {

        OrderSentEvent event = OrderSentEvent.builder()
                .orderId(metadata.orderId())
                .customerName(metadata.customerName())
                .deliveryMethod(metadata.deliveryMethod())
                .items(metadata.items())
                .totalAmount(metadata.totalAmount())
                .pcs(metadata.pcs())
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
