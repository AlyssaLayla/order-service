package com.takehome.orderservice.service;


import com.takehome.orderservice.entity.LineItem;
import com.takehome.orderservice.entity.Order;
import com.takehome.orderservice.entity.OrderStatus;
import com.takehome.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;


    public Order createOrder(
            String customerName,
            List<LineItem> items
    ) {


        BigDecimal totalAmount =
                items.stream()
                        .map(
                                item ->
                                        item.getUnitPrice()
                                                .multiply(
                                                        BigDecimal.valueOf(
                                                                item.getQuantity()
                                                        )
                                                )
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );


        Order order =
                Order.builder()
                        .orderId(
                                UUID.randomUUID()
                        )
                        .customerName(
                                customerName
                        )
                        .items(
                                items
                        )
                        .status(
                                OrderStatus.CREATED
                        )
                        .totalAmount(
                                totalAmount
                        )
                        .build();


        return orderRepository.save(order);
    }
}