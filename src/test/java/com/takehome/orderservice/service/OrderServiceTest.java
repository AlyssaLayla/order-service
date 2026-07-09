package com.takehome.orderservice.service;

import com.takehome.orderservice.entity.LineItem;
import com.takehome.orderservice.entity.Order;
import com.takehome.orderservice.entity.OrderStatus;
import com.takehome.orderservice.repository.OrderRepository;
import com.takehome.orderservice.exception.InvalidOrderException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {


    @Mock
    private OrderRepository orderRepository;


    @InjectMocks
    private OrderService orderService;


    @Test
    void createOrder_shouldCreateOrderWithCreatedStatusAndCalculatedTotal() {

        // Arrange

        List<LineItem> items =
                List.of(
                        LineItem.builder()
                                .productName("Apple")
                                .quantity(3)
                                .unitPrice(
                                        BigDecimal.valueOf(0.50)
                                )
                                .build(),

                        LineItem.builder()
                                .productName("Bread Loaf")
                                .quantity(1)
                                .unitPrice(
                                        BigDecimal.valueOf(2.20)
                                )
                                .build()
                );


        when(
                orderRepository.save(
                        any(Order.class)
                )
        ).thenAnswer(
                invocation ->
                        invocation.getArgument(0)
        );


        // Act

        Order result =
                orderService.createOrder(
                        "Andi Wijaya",
                        items
                );


        // Assert

        assertNotNull(
                result.getOrderId()
        );


        assertEquals(
                "Andi Wijaya",
                result.getCustomerName()
        );


        assertEquals(
                OrderStatus.CREATED,
                result.getStatus()
        );


        assertEquals(
                BigDecimal.valueOf(3.70),
                result.getTotalAmount()
        );


        assertEquals(
                2,
                result.getItems().size()
        );


        verify(
                orderRepository,
                times(1)
        ).save(
                any(Order.class)
        );
    }

    @Test
    void createOrder_withEmptyItems_shouldThrowException() {

        // Arrange

        List<LineItem> items =
                List.of();


        // Act & Assert

        assertThrows(
                InvalidOrderException.class,
                () ->
                        orderService.createOrder(
                                "Andi Wijaya",
                                items
                        )
        );


        verify(
                orderRepository,
                never()
        ).save(
                any(Order.class)
        );
    }

    @Test
    void createOrder_withNegativeQuantity_shouldThrowException() {

        // Arrange

        List<LineItem> items =
                List.of(
                        LineItem.builder()
                                .productName("Apple")
                                .quantity(-3)
                                .unitPrice(
                                        BigDecimal.valueOf(0.50)
                                )
                                .build()
                );


        // Act & Assert

        assertThrows(
                InvalidOrderException.class,
                () ->
                        orderService.createOrder(
                                "Andi Wijaya",
                                items
                        )
        );


        verify(
                orderRepository,
                never()
        ).save(
                any(Order.class)
        );
    }

    @Test
    void createOrder_withNegativeUnitPrice_shouldThrowException() {

        // Arrange

        List<LineItem> items =
                List.of(
                        LineItem.builder()
                                .productName("Apple")
                                .quantity(3)
                                .unitPrice(
                                        BigDecimal.valueOf(-0.50)
                                )
                                .build()
                );


        // Act & Assert

        assertThrows(
                InvalidOrderException.class,
                () ->
                        orderService.createOrder(
                                "Andi Wijaya",
                                items
                        )
        );


        verify(
                orderRepository,
                never()
        ).save(
                any(Order.class)
        );
    }

}