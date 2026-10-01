package com.example.notifications.dto;

import com.example.dto.DeliveryMethod;
import com.example.dto.OrderItem;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record OrderMetadata(

        Long orderId,
        String customerName,
        DeliveryMethod deliveryMethod,
        List<OrderItem> items,
        BigDecimal totalAmount,
        Integer pcs

) {
}
