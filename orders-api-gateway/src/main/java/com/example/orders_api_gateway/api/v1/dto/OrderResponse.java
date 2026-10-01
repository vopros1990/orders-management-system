package com.example.orders_api_gateway.api.v1.dto;

import com.example.dto.DeliveryMethod;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderResponse(
        Long orderId,
        String customerName,
        DeliveryMethod deliveryMethod,
        List<OrderItemResponse> items
) {}
