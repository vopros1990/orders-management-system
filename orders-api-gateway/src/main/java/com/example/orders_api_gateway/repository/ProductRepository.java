package com.example.orders_api_gateway.repository;

import com.example.orders_api_gateway.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {



}
