package com.example.orders_api_gateway.dto;

import com.example.dto.*;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderMetadata(

        Long orderId,
        String customerName,
        DeliveryMethod deliveryMethod,
        List<OrderItem> items

) {
}
