package com.example.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record OrderPayedEvent(
        Long orderId,
        String customerName,
        DeliveryMethod deliveryMethod,
        List<OrderItem> items,
        BigDecimal totalAmount
) {
}
