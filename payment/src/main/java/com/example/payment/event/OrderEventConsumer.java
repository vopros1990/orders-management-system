package com.example.payment.event;

import com.example.dto.OrderConfirmedEvent;
import com.example.payment.dto.OrderMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final OrderEventProducer producer;

    @KafkaListener(topics = "${app.consumer.topic-name}")
    public void listen(ConsumerRecord<String, OrderConfirmedEvent> record, Acknowledgment acknowledgment) {

        OrderConfirmedEvent orderData = record.value();

        BigDecimal totalAmount = orderData.items().stream()
                .reduce(BigDecimal.ZERO, (total, item) -> total.add(item.unitPrice()), BigDecimal::add);

        OrderMetadata metadata = OrderMetadata.builder()
                .orderId(orderData.orderId())
                .customerName(orderData.customerName())
                .deliveryMethod(orderData.deliveryMethod())
                .items(orderData.items())
                .totalAmount(totalAmount)
                .build();

        producer.send(metadata);

        acknowledgment.acknowledge();

        log.info("Consumed OrderConfirmed event. Topic: {}, partition: {}, offset: {}, key: {}, orderId: {}. \n " +
                        "TOTAL ORDER AMOUNT: {}",
                record.topic(),
                record.partition(),
                record.offset(),
                record.key(),
                record.value().orderId(),
                totalAmount
        );

    }

}
