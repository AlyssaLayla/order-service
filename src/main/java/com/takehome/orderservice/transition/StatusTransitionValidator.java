package com.takehome.orderservice.transition;


import com.takehome.orderservice.entity.OrderStatus;
import com.takehome.orderservice.exception.InvalidStatusTransitionException;

import org.springframework.stereotype.Component;


import java.util.Map;
import java.util.Set;


@Component
public class StatusTransitionValidator {


    private final Map<OrderStatus, Set<OrderStatus>>
            allowedTransitions =
            Map.of(

                    OrderStatus.CREATED,
                    Set.of(
                            OrderStatus.PAID,
                            OrderStatus.CANCELLED
                    ),

                    OrderStatus.PAID,
                    Set.of(
                            OrderStatus.SHIPPED
                    ),

                    OrderStatus.SHIPPED,
                    Set.of(
                            OrderStatus.DELIVERED
                    ),

                    OrderStatus.DELIVERED,
                    Set.of(),

                    OrderStatus.CANCELLED,
                    Set.of()
            );


    public void validate(
            OrderStatus currentStatus,
            OrderStatus newStatus
    ) {


        boolean allowed =
                allowedTransitions
                        .get(
                                currentStatus
                        )
                        .contains(
                                newStatus
                        );


        if (!allowed) {


            throw new InvalidStatusTransitionException(
                    "Invalid status transition from "
                            + currentStatus
                            + " to "
                            + newStatus
            );
        }
    }
}