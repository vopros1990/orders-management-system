package com.example.notifications.event;

import com.example.dto.OrderSentEvent;
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

    @KafkaListener(topics = "${app.consumer.topic-name}")
    public void listen(ConsumerRecord<String, OrderSentEvent> record, Acknowledgment acknowledgment) {

        OrderSentEvent orderData = record.value();

        acknowledgment.acknowledge();

        log.info("Consumed OrderSent event. Topic: {}, partition: {}, offset: {}, key: {}, orderId: {}. \n " +
                        "SENT NOTIFICATION TO USER {}. \n" +
                        "ORDER ITEMS: {}",
                record.topic(),
                record.partition(),
                record.offset(),
                record.key(),
                record.value().orderId(),
                orderData.customerName(),
                orderData.items()
        );

    }

}
