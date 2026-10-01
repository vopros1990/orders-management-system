package com.example.payment.dto;

import lombok.Builder;

import com.example.dto.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record OrderMetadata(

        Long orderId,
        String customerName,
        DeliveryMethod deliveryMethod,
        List<OrderItem> items,
        BigDecimal totalAmount

) {
}
