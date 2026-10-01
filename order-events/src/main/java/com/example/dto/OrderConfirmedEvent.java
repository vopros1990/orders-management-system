package com.example.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderConfirmedEvent(
        Long orderId,
        String customerName,
        DeliveryMethod deliveryMethod,
        List<OrderItem> items
) {
}
