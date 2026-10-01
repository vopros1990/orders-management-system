package com.example.notifications.configuration;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.consumer")
@Validated
public record AppConsumerProperties(

        @NotNull
        @Min(1)
        Integer concurrency,

        @NotNull
        String topicName,

        @NotNull
        String groupId,

        @NotNull
        @Min(1L)
        Long backoffMs,

        @NotNull
        @Min(1)
        Integer maxRetryAttempts

) {}
