package com.takehome.orderservice.dto.request;


import java.util.List;


public record UpdateOrderRequest(

        String customerName,

        List<LineItemRequest> items
) {
}