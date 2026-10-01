package com.example.orders_api_gateway.exception;

import com.example.orders_api_gateway.dto.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestExceptions(Exception e) {

        return ErrorResponse.of(e.getMessage(), HttpStatus.BAD_REQUEST.value());

    }

    @ExceptionHandler({
            ConstraintViolationException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationExceptionException(ConstraintViolationException e) {

        List<String> messages = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();

        return messages.size() == 1 ?
                ErrorResponse.of(messages.getFirst(), HttpStatus.BAD_REQUEST.value()) :
                ErrorResponse.of(messages, HttpStatus.BAD_REQUEST.value());

    }

    @ExceptionHandler({
            EntityNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFountException(Exception e) {

        return ErrorResponse.of(e.getMessage(), HttpStatus.NOT_FOUND.value());

    }

}

