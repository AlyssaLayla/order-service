package com.takehome.orderservice.service;


import com.takehome.orderservice.entity.LineItem;
import com.takehome.orderservice.entity.Order;
import com.takehome.orderservice.entity.OrderStatus;

import com.takehome.orderservice.exception.InvalidOrderException;
import com.takehome.orderservice.exception.OrderNotFoundException;
import com.takehome.orderservice.transition.StatusTransitionValidator;
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
    private final StatusTransitionValidator statusTransitionValidator;

    public Order createOrder(
            String customerName,
            List<LineItem> items
    ) {


        validateOrder(
                items
        );


        BigDecimal totalAmount =
                calculateTotalAmount(
                        items
                );


        Order order =
                Order.builder()
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


        return orderRepository.save(
                order
        );
    }


    public Order getOrderById(
            UUID orderId
    ) {


        return orderRepository
                .findById(
                        orderId
                )
                .orElseThrow(
                        () ->
                                new OrderNotFoundException(
                                        "Order not found"
                                )
                );
    }


    public List<Order> getOrders() {


        return orderRepository
                .findAll();
    }

    public Order updateOrder(
            UUID orderId,
            String customerName,
            List<LineItem> items
    ) {


        validateOrder(
                items
        );


        Order existingOrder =
                getOrderById(
                        orderId
                );


        existingOrder.setCustomerName(
                customerName
        );


        existingOrder.setItems(
                items
        );


        existingOrder.setTotalAmount(
                calculateTotalAmount(
                        items
                )
        );


        return orderRepository.save(
                existingOrder
        );
    }

    public void deleteOrder(
            UUID orderId
    ) {


        Order existingOrder =
                getOrderById(
                        orderId
                );


        orderRepository.delete(
                existingOrder
        );
    }

    public Order updateStatus(
            UUID orderId,
            OrderStatus newStatus
    ) {


        Order order =
                getOrderById(
                        orderId
                );


        statusTransitionValidator.validate(
                order.getStatus(),
                newStatus
        );


        order.setStatus(
                newStatus
        );


        return orderRepository.save(
                order
        );
    }

    private BigDecimal calculateTotalAmount(
            List<LineItem> items
    ) {


        return items.stream()
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


        for (
                LineItem item :
                items
        ) {


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
                                    .compareTo(
                                            BigDecimal.ZERO
                                    ) < 0
            ) {


                throw new InvalidOrderException(
                        "Item price cannot be negative"
                );
            }
        }
    }
}