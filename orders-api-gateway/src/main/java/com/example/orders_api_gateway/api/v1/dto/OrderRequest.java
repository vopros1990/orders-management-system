package com.example.orders_api_gateway.api.v1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record OrderRequest(

        @NotBlank(message = "Укажите на чье имя оформляется заказ")
        String customerName,

        @Pattern(regexp = "POST|COURIER|PICKUP_POINT|EXPRESS")
        @NotNull(message = "Укажите способ доставки")
        String deliveryMethod

) {
}
