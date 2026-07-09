package com.takehome.orderservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.takehome.orderservice.entity.LineItem;
import com.takehome.orderservice.entity.Order;
import com.takehome.orderservice.entity.OrderStatus;

import com.takehome.orderservice.service.OrderService;


import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;

import java.util.List;
import java.util.UUID;
import java.util.Arrays;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(OrderController.class)
class OrderControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private ObjectMapper objectMapper;


    @MockitoBean
    private OrderService orderService;


    @Test
    void createOrder_shouldReturnCreatedOrder()
            throws Exception {


        // Arrange

        Order order =
                Order.builder()
                        .orderId(
                                UUID.randomUUID()
                        )
                        .customerName(
                                "Andi Wijaya"
                        )
                        .items(
                                List.of(
                                        LineItem.builder()
                                                .productName(
                                                        "Apple"
                                                )
                                                .quantity(
                                                        2
                                                )
                                                .unitPrice(
                                                        BigDecimal.valueOf(5)
                                                )
                                                .build()
                                )
                        )
                        .status(
                                OrderStatus.CREATED
                        )
                        .totalAmount(
                                BigDecimal.valueOf(10)
                        )
                        .build();


        when(
                orderService.createOrder(
                        any(),
                        any()
                )
        ).thenReturn(
                order
        );


        String request =
                """
                {
                    "customerName":"Andi Wijaya",
                    "items":[
                        {
                            "productName":"Apple",
                            "quantity":2,
                            "unitPrice":5
                        }
                    ]
                }
                """;


        // Act & Assert

        mockMvc.perform(
                        post("/api/orders")
                                .contentType(
                                        "application/json"
                                )
                                .content(
                                        request
                                )
                )

                .andExpect(
                        status()
                                .isCreated()
                )

                .andExpect(
                        jsonPath("$.orderId")
                                .exists()
                )

                .andExpect(
                        jsonPath("$.customerName")
                                .value(
                                        "Andi Wijaya"
                                )
                )

                .andExpect(
                        jsonPath("$.items.length()")
                                .value(
                                        1
                                )
                )

                .andExpect(
                        jsonPath("$.items[0].productName")
                                .value(
                                        "Apple"
                                )
                )

                .andExpect(
                        jsonPath("$.status")
                                .value(
                                        "CREATED"
                                )
                )

                .andExpect(
                        jsonPath("$.totalAmount")
                                .value(
                                        10
                                )
                );


        verify(
                orderService,
                times(1)
        ).createOrder(
                any(),
                any()
        );
    }

    @Test
    void getOrderById_shouldReturnOrder()
            throws Exception {


        // Arrange

        UUID orderId =
                UUID.randomUUID();


        Order order =
                Order.builder()
                        .orderId(orderId)
                        .customerName("Andi Wijaya")
                        .items(
                                List.of(
                                        LineItem.builder()
                                                .productName("Apple")
                                                .quantity(2)
                                                .unitPrice(
                                                        BigDecimal.valueOf(5)
                                                )
                                                .build()
                                )
                        )
                        .status(OrderStatus.CREATED)
                        .totalAmount(
                                BigDecimal.valueOf(10)
                        )
                        .build();


        when(
                orderService.getOrderById(
                        orderId
                )
        ).thenReturn(
                order
        );


        // Act & Assert

        mockMvc.perform(
                        get(
                                "/api/orders/{id}",
                                orderId
                        )
                )

                .andExpect(
                        status()
                                .isOk()
                )

                .andExpect(
                        jsonPath("$.orderId")
                                .value(
                                        orderId.toString()
                                )
                )

                .andExpect(
                        jsonPath("$.customerName")
                                .value(
                                        "Andi Wijaya"
                                )
                );
    }

    @Test
    void getOrders_shouldReturnOrders()
            throws Exception {


        // Arrange

        List<Order> orders =
                List.of(
                        Order.builder()
                                .orderId(
                                        UUID.randomUUID()
                                )
                                .customerName(
                                        "Andi"
                                )
                                .items(
                                        List.of()
                                )
                                .status(
                                        OrderStatus.CREATED
                                )
                                .totalAmount(
                                        BigDecimal.ZERO
                                )
                                .build(),

                        Order.builder()
                                .orderId(
                                        UUID.randomUUID()
                                )
                                .customerName(
                                        "Budi"
                                )
                                .items(
                                        List.of()
                                )
                                .status(
                                        OrderStatus.CREATED
                                )
                                .totalAmount(
                                        BigDecimal.ZERO
                                )
                                .build()
                );


        when(
                orderService.getOrders()
        ).thenReturn(
                orders
        );


        mockMvc.perform(
                        get(
                                "/api/orders"
                        )
                )

                .andExpect(
                        status()
                                .isOk()
                )

                .andExpect(
                        jsonPath("$.length()")
                                .value(
                                        2
                                )
                );
    }

    @Test
    void updateOrder_shouldReturnUpdatedOrder()
            throws Exception {


        UUID orderId =
                UUID.randomUUID();


        Order updated =
                Order.builder()
                        .orderId(
                                orderId
                        )
                        .customerName(
                                "Updated Customer"
                        )
                        .items(
                                List.of(
                                        LineItem.builder()
                                                .productName(
                                                        "Bread"
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
                        .status(
                                OrderStatus.CREATED
                        )
                        .totalAmount(
                                BigDecimal.valueOf(5)
                        )
                        .build();


        when(
                orderService.updateOrder(
                        any(),
                        any(),
                        any()
                )
        ).thenReturn(
                updated
        );


        String request =
                """
                {
                    "customerName":"Updated Customer",
                    "items":[
                        {
                            "productName":"Bread",
                            "quantity":1,
                            "unitPrice":5
                        }
                    ]
                }
                """;


        mockMvc.perform(
                        put(
                                "/api/orders/{id}",
                                orderId
                        )
                                .contentType(
                                        "application/json"
                                )
                                .content(
                                        request
                                )
                )

                .andExpect(
                        status()
                                .isOk()
                )

                .andExpect(
                        jsonPath("$.customerName")
                                .value(
                                        "Updated Customer"
                                )
                );
    }

    @Test
    void deleteOrder_shouldReturnNoContent()
            throws Exception {


        UUID orderId =
                UUID.randomUUID();


        doNothing()
                .when(
                        orderService
                )
                .deleteOrder(
                        orderId
                );


        mockMvc.perform(
                        delete(
                                "/api/orders/{id}",
                                orderId
                        )
                )

                .andExpect(
                        status()
                                .isNoContent()
                );


        verify(
                orderService
        ).deleteOrder(
                orderId
        );
    }
}