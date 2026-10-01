package com.example.orders_api_gateway.configuration;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.producer")
@Validated
public record AppProducerProperties(

        @NotNull
        String topicName,

        @NotNull
        @Min(1)
        Integer partitions,

        @NotNull
        @Min(1)
        Integer replicas

) {
}
