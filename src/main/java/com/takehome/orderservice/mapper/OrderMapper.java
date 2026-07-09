package com.takehome.orderservice.mapper;


import com.takehome.orderservice.dto.request.LineItemRequest;

import com.takehome.orderservice.dto.response.LineItemResponse;
import com.takehome.orderservice.dto.response.OrderResponse;

import com.takehome.orderservice.entity.LineItem;
import com.takehome.orderservice.entity.Order;


import java.util.List;


public class OrderMapper {


    public static List<LineItem> toItems(
            List<LineItemRequest> request
    ) {

        return request.stream()
                .map(
                        item ->
                                LineItem.builder()
                                        .productName(
                                                item.productName()
                                        )
                                        .quantity(
                                                item.quantity()
                                        )
                                        .unitPrice(
                                                item.unitPrice()
                                        )
                                        .build()
                )
                .toList();
    }


    public static OrderResponse toResponse(
            Order order
    ) {


        return new OrderResponse(
                order.getOrderId(),

                order.getCustomerName(),

                order.getItems()
                        .stream()
                        .map(
                                item ->
                                        new LineItemResponse(
                                                item.getProductName(),
                                                item.getQuantity(),
                                                item.getUnitPrice()
                                        )
                        )
                        .toList(),

                order.getStatus(),

                order.getTotalAmount()
        );
    }
}