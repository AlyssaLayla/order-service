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
}