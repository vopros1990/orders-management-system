package com.example.orders_api_gateway.service;

import com.example.dto.DeliveryMethod;
import com.example.orders_api_gateway.api.v1.dto.AddProductRequest;
import com.example.orders_api_gateway.api.v1.dto.OrderRequest;
import com.example.orders_api_gateway.api.v1.dto.OrderResponse;
import com.example.orders_api_gateway.dto.OrderMetadata;
import com.example.orders_api_gateway.entity.Order;
import com.example.orders_api_gateway.entity.OrderItem;
import com.example.orders_api_gateway.entity.Product;
import com.example.orders_api_gateway.event.OrderEventProducer;
import com.example.orders_api_gateway.mapping.OrderMapper;
import com.example.orders_api_gateway.repository.OrderItemRepository;
import com.example.orders_api_gateway.repository.OrderRepository;
import com.example.orders_api_gateway.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class OrdersService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final ProductRepository productRepository;

    private final OrderEventProducer eventService;

    public @Nullable OrderResponse create(@Valid OrderRequest request) {

        Order order = Order.builder()
                .customerName(request.customerName())
                .deliveryMethod(DeliveryMethod.valueOf(request.deliveryMethod()))
                .build();

        Order placedOrder = orderRepository.save(order);

        return OrderMapper.toResponse(placedOrder);
    }

    @Transactional
    public void addItem(@Valid AddProductRequest request, @Valid Long orderId) {

        if (!orderRepository.existsById(orderId))
            throw new EntityNotFoundException("Заказ не найден");

        if (!productRepository.existsById(request.productId()))
            throw new EntityNotFoundException("Товар не найден");

        Order order = orderRepository.getReferenceById(orderId);

        Product product = productRepository.getReferenceById(request.productId());

        OrderItem item = OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(request.quantity())
                .build();

        orderItemRepository.save(item);

    }

    public OrderResponse confirm(@Valid Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Заказ не найден"));

        OrderMetadata metadata = OrderMapper.toMetadata(order);

        eventService.send(metadata);

        return OrderMapper.toResponse(order);

    }

}
