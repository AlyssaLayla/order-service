package com.takehome.orderservice.service;


import com.takehome.orderservice.entity.LineItem;
import com.takehome.orderservice.entity.Order;
import com.takehome.orderservice.entity.OrderStatus;
import com.takehome.orderservice.repository.OrderRepository;
import com.takehome.orderservice.exception.InvalidOrderException;

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

        validateOrder(
                items
        );


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

    private void validateOrder(
            List<LineItem> items
    ) {


        if (
                items == null ||
                        items.isEmpty()
        ) {

            throw new InvalidOrderException(
                    "Order must contain at least one item"
            );
        }


        for (LineItem item : items) {


            if (
                    item.getQuantity() == null ||
                            item.getQuantity() <= 0
            ) {

                throw new InvalidOrderException(
                        "Item quantity must be positive"
                );
            }


            if (
                    item.getUnitPrice() == null ||
                            item.getUnitPrice()
                                    .compareTo(BigDecimal.ZERO)
                                    < 0
            ) {

                throw new InvalidOrderException(
                        "Item price cannot be negative"
                );
            }
        }
    }

}