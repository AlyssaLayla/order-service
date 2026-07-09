package com.takehome.orderservice.dto.request;


import java.math.BigDecimal;


public record LineItemRequest(

        String productName,

        Integer quantity,

        BigDecimal unitPrice
) {
}