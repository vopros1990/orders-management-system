package com.example.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItem(
        Long productId,
        String productName,
        BigDecimal unitPrice,
        Integer quantity
) {
}
