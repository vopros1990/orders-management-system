package com.example.orders_api_gateway.repository;

import com.example.orders_api_gateway.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
