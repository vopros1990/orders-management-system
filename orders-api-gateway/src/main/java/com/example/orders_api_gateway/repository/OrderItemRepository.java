package com.example.orders_api_gateway.repository;

import com.example.orders_api_gateway.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {



}
