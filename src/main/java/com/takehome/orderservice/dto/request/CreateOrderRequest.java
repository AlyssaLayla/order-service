package com.takehome.orderservice.dto.request;


import java.util.List;


public record CreateOrderRequest(

        String customerName,

        List<LineItemRequest> items
) {
}