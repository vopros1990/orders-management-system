package com.example.shipping.event;

import com.example.dto.*;
import com.example.shipping.dto.OrderMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final OrderEventProducer producer;

    @KafkaListener(topics = "${app.consumer.topic-name}")
    public void listen(ConsumerRecord<String, OrderPayedEvent> record, Acknowledgment acknowledgment) {

        OrderPayedEvent orderData = record.value();

        Integer pcs = orderData.items().stream()
                .mapToInt(OrderItem::quantity)
                .sum();

        OrderMetadata metadata = OrderMetadata.builder()
                .orderId(orderData.orderId())
                .customerName(orderData.customerName())
                .deliveryMethod(orderData.deliveryMethod())
                .items(orderData.items())
                .totalAmount(orderData.totalAmount())
                .pcs(pcs)
                .build();

        producer.send(metadata);

        acknowledgment.acknowledge();

        log.info("Consumed OrderPayed event. Topic: {}, partition: {}, offset: {}, key: {}, orderId: {}. \n " +
                        "SENT {} pcs WITH {} method",
                record.topic(),
                record.partition(),
                record.offset(),
                record.key(),
                record.value().orderId(),
                pcs,
                orderData.deliveryMethod()
        );

    }

}
