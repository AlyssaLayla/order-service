package com.takehome.orderservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(
            OrderNotFoundException exception
    ) {


        ErrorResponse response =
                new ErrorResponse(
                        LocalDateTime.now(),

                        HttpStatus.NOT_FOUND.value(),

                        "ORDER_NOT_FOUND",

                        exception.getMessage()
                );


        return ResponseEntity
                .status(
                        HttpStatus.NOT_FOUND
                )
                .body(
                        response
                );
    }


    @ExceptionHandler(InvalidOrderException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOrder(
            InvalidOrderException exception
    ) {


        ErrorResponse response =
                new ErrorResponse(
                        LocalDateTime.now(),

                        HttpStatus.BAD_REQUEST.value(),

                        "INVALID_ORDER",

                        exception.getMessage()
                );


        return ResponseEntity
                .badRequest()
                .body(
                        response
                );
    }
}