package com.example.orders_api_gateway.configuration;

import com.example.orders_api_gateway.entity.Product;
import com.example.orders_api_gateway.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ProductCatalogConfig {

    private final ProductRepository repository;

    @PostConstruct
    public void addProducts() {

        List<Product> products = List.of(
                new Product(null, "Iphone 15", new BigDecimal("45000")),
                new Product(null, "Macbook Pro 2025", new BigDecimal("145000")),
                new Product(null, "Samsung Smart TV", new BigDecimal("42000")),
                new Product(null, "Flash 16Gb", new BigDecimal("500"))
        );

        repository.saveAll(products);

    }

}
