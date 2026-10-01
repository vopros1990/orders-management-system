package com.example.orders_api_gateway.dto;

public record ErrorResponse(

        Object message,
        int code

) {

    public static ErrorResponse of(Object message, int code) {

        return new ErrorResponse(message, code);

    }

}
