package com.takehome.orderservice.dto.request;

import com.takehome.orderservice.entity.OrderStatus;

public record UpdateStatusRequest(

        OrderStatus status
) {
}