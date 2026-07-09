package com.takehome.orderservice.controller;


import com.takehome.orderservice.dto.request.CreateOrderRequest;

import com.takehome.orderservice.dto.response.OrderResponse;

import com.takehome.orderservice.mapper.OrderMapper;

import com.takehome.orderservice.service.OrderService;


import lombok.RequiredArgsConstructor;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(
            @RequestBody CreateOrderRequest request
    ) {


        var order =
                orderService.createOrder(
                        request.customerName(),

                        OrderMapper.toItems(
                                request.items()
                        )
                );


        return OrderMapper.toResponse(
                order
        );
    }
}