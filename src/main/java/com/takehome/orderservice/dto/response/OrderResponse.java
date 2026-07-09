package com.takehome.orderservice.dto.response;


import com.takehome.orderservice.entity.OrderStatus;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public record OrderResponse(

        UUID orderId,

        String customerName,

        List<LineItemResponse> items,

        OrderStatus status,

        BigDecimal totalAmount
) {
}