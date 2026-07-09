package com.takehome.orderservice.service;

import com.takehome.orderservice.entity.LineItem;
import com.takehome.orderservice.entity.Order;
import com.takehome.orderservice.entity.OrderStatus;
import com.takehome.orderservice.repository.OrderRepository;
import com.takehome.orderservice.exception.InvalidOrderException;
import com.takehome.orderservice.exception.OrderNotFoundException;
import com.takehome.orderservice.transition.StatusTransitionValidator;
import com.takehome.orderservice.exception.InvalidStatusTransitionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {


    @Mock
    private OrderRepository orderRepository;

    @Spy
    private StatusTransitionValidator statusTransitionValidator =
            new StatusTransitionValidator();

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
                invocation -> {

                    Order savedOrder =
                            invocation.getArgument(0);

                    savedOrder.setOrderId(
                            UUID.randomUUID()
                    );

                    return savedOrder;
                }
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

    @Test
    void getOrderById_withExistingId_shouldReturnOrder() {

        // Arrange

        UUID orderId =
                UUID.randomUUID();


        Order order =
                Order.builder()
                        .orderId(orderId)
                        .customerName("Andi Wijaya")
                        .status(OrderStatus.CREATED)
                        .totalAmount(
                                BigDecimal.valueOf(10)
                        )
                        .build();


        when(
                orderRepository.findById(orderId)
        ).thenReturn(
                Optional.of(order)
        );


        // Act

        Order result =
                orderService.getOrderById(
                        orderId
                );


        // Assert

        assertEquals(
                orderId,
                result.getOrderId()
        );


        assertEquals(
                "Andi Wijaya",
                result.getCustomerName()
        );


        verify(
                orderRepository
        ).findById(orderId);
    }

    @Test
    void getOrderById_withUnknownId_shouldThrowException() {

        // Arrange

        UUID orderId =
                UUID.randomUUID();


        when(
                orderRepository.findById(orderId)
        ).thenReturn(
                Optional.empty()
        );


        // Act & Assert

        assertThrows(
                OrderNotFoundException.class,
                () ->
                        orderService.getOrderById(
                                orderId
                        )
        );


        verify(
                orderRepository
        ).findById(orderId);
    }

    @Test
    void getOrders_shouldReturnAllOrders() {

        // Arrange

        List<Order> orders =
                List.of(
                        Order.builder()
                                .customerName("Andi")
                                .build(),

                        Order.builder()
                                .customerName("Budi")
                                .build()
                );


        when(
                orderRepository.findAll()
        ).thenReturn(
                orders
        );


        // Act

        List<Order> result =
                orderService.getOrders();


        // Assert

        assertEquals(
                2,
                result.size()
        );


        verify(
                orderRepository
        ).findAll();
    }

    @Test
    void updateOrder_withExistingOrder_shouldUpdateOrderDetails() {

        // Arrange

        UUID orderId =
                UUID.randomUUID();


        Order existingOrder =
                Order.builder()
                        .orderId(orderId)
                        .customerName("Old Customer")
                        .items(
                                List.of(
                                        LineItem.builder()
                                                .productName("Old Item")
                                                .quantity(1)
                                                .unitPrice(
                                                        BigDecimal.valueOf(10)
                                                )
                                                .build()
                                )
                        )
                        .status(OrderStatus.CREATED)
                        .totalAmount(
                                BigDecimal.valueOf(10)
                        )
                        .build();


        List<LineItem> updatedItems =
                List.of(
                        LineItem.builder()
                                .productName("Apple")
                                .quantity(3)
                                .unitPrice(
                                        BigDecimal.valueOf(2)
                                )
                                .build()
                );


        when(
                orderRepository.findById(orderId)
        ).thenReturn(
                Optional.of(existingOrder)
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
                orderService.updateOrder(
                        orderId,
                        "New Customer",
                        updatedItems
                );


        // Assert

        assertEquals(
                "New Customer",
                result.getCustomerName()
        );


        assertEquals(
                1,
                result.getItems().size()
        );


        assertEquals(
                BigDecimal.valueOf(6),
                result.getTotalAmount()
        );


        verify(
                orderRepository
        ).save(
                existingOrder
        );
    }

    @Test
    void updateOrder_withUnknownId_shouldThrowException() {

        // Arrange

        UUID orderId =
                UUID.randomUUID();


        List<LineItem> items =
                List.of(
                        LineItem.builder()
                                .productName("Apple")
                                .quantity(1)
                                .unitPrice(
                                        BigDecimal.valueOf(2)
                                )
                                .build()
                );


        when(
                orderRepository.findById(orderId)
        ).thenReturn(
                Optional.empty()
        );


        // Act & Assert

        assertThrows(
                OrderNotFoundException.class,
                () ->
                        orderService.updateOrder(
                                orderId,
                                "Customer",
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
    void deleteOrder_withExistingId_shouldDeleteOrder() {

        // Arrange

        UUID orderId =
                UUID.randomUUID();


        Order existingOrder =
                Order.builder()
                        .orderId(orderId)
                        .customerName("Andi Wijaya")
                        .status(OrderStatus.CREATED)
                        .build();


        when(
                orderRepository.findById(
                        orderId
                )
        ).thenReturn(
                Optional.of(existingOrder)
        );


        // Act

        orderService.deleteOrder(
                orderId
        );


        // Assert

        verify(
                orderRepository,
                times(1)
        ).delete(
                existingOrder
        );
    }

    @Test
    void deleteOrder_withUnknownId_shouldThrowException() {

        // Arrange

        UUID orderId =
                UUID.randomUUID();


        when(
                orderRepository.findById(
                        orderId
                )
        ).thenReturn(
                Optional.empty()
        );


        // Act & Assert

        assertThrows(
                OrderNotFoundException.class,
                () ->
                        orderService.deleteOrder(
                                orderId
                        )
        );


        verify(
                orderRepository,
                never()
        ).delete(
                any(Order.class)
        );
    }

    @Test
    void updateStatus_withValidTransition_shouldUpdateOrderStatus() {


        // Arrange

        UUID orderId =
                UUID.randomUUID();


        Order order =
                Order.builder()
                        .orderId(orderId)
                        .status(
                                OrderStatus.CREATED
                        )
                        .build();


        when(
                orderRepository.findById(
                        orderId
                )
        ).thenReturn(
                Optional.of(order)
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
                orderService.updateStatus(
                        orderId,
                        OrderStatus.PAID
                );


        // Assert

        assertEquals(
                OrderStatus.PAID,
                result.getStatus()
        );


        verify(
                statusTransitionValidator
        ).validate(
                OrderStatus.CREATED,
                OrderStatus.PAID
        );


        verify(
                orderRepository
        ).save(order);
    }

    @Test
    void updateStatus_withInvalidTransition_shouldThrowException() {


        // Arrange

        UUID orderId =
                UUID.randomUUID();


        Order order =
                Order.builder()
                        .orderId(orderId)
                        .status(
                                OrderStatus.CREATED
                        )
                        .build();


        when(
                orderRepository.findById(
                        orderId
                )
        ).thenReturn(
                Optional.of(order)
        );


        // Act & Assert

        assertThrows(
                InvalidStatusTransitionException.class,
                () ->
                        orderService.updateStatus(
                                orderId,
                                OrderStatus.SHIPPED
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
    void updateStatus_toCancelledWithReason_shouldCancelOrder() {


        // Arrange

        UUID orderId =
                UUID.randomUUID();


        Order order =
                Order.builder()
                        .orderId(orderId)
                        .status(
                                OrderStatus.CREATED
                        )
                        .build();


        when(
                orderRepository.findById(
                        orderId
                )
        ).thenReturn(
                Optional.of(order)
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
                orderService.updateStatus(
                        orderId,
                        OrderStatus.CANCELLED,
                        "Customer changed mind"
                );


        // Assert

        assertEquals(
                OrderStatus.CANCELLED,
                result.getStatus()
        );


        assertEquals(
                "Customer changed mind",
                result.getCancellationReason()
        );


        verify(
                orderRepository
        ).save(order);
    }

    @Test
    void updateStatus_toCancelledWithoutReason_shouldThrowException() {


        // Arrange

        UUID orderId =
                UUID.randomUUID();


        Order order =
                Order.builder()
                        .orderId(orderId)
                        .status(
                                OrderStatus.CREATED
                        )
                        .build();


        when(
                orderRepository.findById(
                        orderId
                )
        ).thenReturn(
                Optional.of(order)
        );


        // Act & Assert

        assertThrows(
                InvalidOrderException.class,
                () ->
                        orderService.updateStatus(
                                orderId,
                                OrderStatus.CANCELLED,
                                null
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
    void updateOrder_withPaidOrder_shouldThrowException() {


        // Arrange

        UUID orderId =
                UUID.randomUUID();


        Order existingOrder =
                Order.builder()
                        .orderId(
                                orderId
                        )
                        .customerName(
                                "Andi Wijaya"
                        )
                        .status(
                                OrderStatus.PAID
                        )
                        .items(
                                List.of(
                                        LineItem.builder()
                                                .productName(
                                                        "Apple"
                                                )
                                                .quantity(
                                                        1
                                                )
                                                .unitPrice(
                                                        BigDecimal.valueOf(5)
                                                )
                                                .build()
                                )
                        )
                        .build();


        List<LineItem> updatedItems =
                List.of(
                        LineItem.builder()
                                .productName(
                                        "Bread"
                                )
                                .quantity(
                                        2
                                )
                                .unitPrice(
                                        BigDecimal.valueOf(10)
                                )
                                .build()
                );


        when(
                orderRepository.findById(
                        orderId
                )
        ).thenReturn(
                Optional.of(existingOrder)
        );


        // Act & Assert

        assertThrows(
                InvalidOrderException.class,
                () ->
                        orderService.updateOrder(
                                orderId,
                                "New Customer",
                                updatedItems
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