package com.example.orders_api_gateway.mapping;

import com.example.orders_api_gateway.api.v1.dto.OrderItemResponse;
import com.example.orders_api_gateway.api.v1.dto.OrderResponse;
import com.example.orders_api_gateway.dto.OrderMetadata;
import com.example.orders_api_gateway.entity.Order;
import com.example.orders_api_gateway.entity.OrderItem;

import java.util.List;

public class OrderMapper {

    public static OrderResponse toResponse(Order order) {

        List<OrderItemResponse> items;

        if (order.getItems() != null)
            items = order.getItems().stream().map(OrderMapper::toItemResponse).toList();
        else
            items = List.of();

        return OrderResponse.builder()
                .orderId(order.getId())
                .customerName(order.getCustomerName())
                .deliveryMethod(order.getDeliveryMethod())
                .items(items)
                .build();

    }

    public static OrderItemResponse toItemResponse(OrderItem item) {

        return OrderItemResponse.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .unitPrice(item.getProduct().getPrice())
                .quantity(item.getQuantity())
                .build();

    }

    public static OrderMetadata toMetadata(Order order) {

        List<com.example.dto.OrderItem> items;

        if (order.getItems() != null)
            items = order.getItems().stream().map(OrderMapper::toItemDto).toList();
        else
            items = List.of();

        return OrderMetadata.builder()
                .orderId(order.getId())
                .customerName(order.getCustomerName())
                .deliveryMethod(order.getDeliveryMethod())
                .items(items)
                .build();

    }

    public static com.example.dto.OrderItem toItemDto(OrderItem item) {

        return com.example.dto.OrderItem.builder()
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .unitPrice(item.getProduct().getPrice())
                .quantity(item.getQuantity())
                .build();

    }

}
