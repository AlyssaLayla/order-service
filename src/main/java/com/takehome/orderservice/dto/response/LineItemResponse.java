package com.takehome.orderservice.dto.response;


import java.math.BigDecimal;


public record LineItemResponse(

        String productName,

        Integer quantity,

        BigDecimal unitPrice
) {
}