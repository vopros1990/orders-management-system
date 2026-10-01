package com.example.orders_api_gateway.api.v1.dto;

import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Range;

public record AddProductRequest(

        @Min(value = 1L, message = "Укажите корректный id товара")
        Long productId,

        @Range(min = 1, max = 5, message = "Количество должно быть от 1 до 5")
        Integer quantity

) {
}
