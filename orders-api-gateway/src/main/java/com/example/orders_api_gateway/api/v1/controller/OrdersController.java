package com.example.orders_api_gateway.api.v1.controller;

import com.example.orders_api_gateway.api.v1.dto.AddProductRequest;
import com.example.orders_api_gateway.api.v1.dto.OrderRequest;
import com.example.orders_api_gateway.api.v1.dto.OrderResponse;
import com.example.orders_api_gateway.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService service;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest request) {

        return ResponseEntity.ok(
                service.create(request)
        );

    }

    @PatchMapping("/{orderId}/add-item")
    public ResponseEntity<Void> addItem(@RequestBody AddProductRequest request, @PathVariable Long orderId) {

        service.addItem(request, orderId);

        return ResponseEntity.noContent().build();

    }

    @PatchMapping("/{orderId}/confirm")
    public ResponseEntity<OrderResponse> confirm(@PathVariable Long orderId) {

        return ResponseEntity.ok(
                service.confirm(orderId)
        );

    }

}
