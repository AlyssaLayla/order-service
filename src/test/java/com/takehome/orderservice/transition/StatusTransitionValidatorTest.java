package com.takehome.orderservice.transition;


import com.takehome.orderservice.entity.OrderStatus;
import com.takehome.orderservice.exception.InvalidStatusTransitionException;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


class StatusTransitionValidatorTest {


    private final StatusTransitionValidator validator =
            new StatusTransitionValidator();


    @Test
    void validateTransition_fromCreatedToPaid_shouldPass() {


        assertDoesNotThrow(
                () ->
                        validator.validate(
                                OrderStatus.CREATED,
                                OrderStatus.PAID
                        )
        );
    }


    @Test
    void validateTransition_fromPaidToShipped_shouldPass() {


        assertDoesNotThrow(
                () ->
                        validator.validate(
                                OrderStatus.PAID,
                                OrderStatus.SHIPPED
                        )
        );
    }


    @Test
    void validateTransition_fromShippedToDelivered_shouldPass() {


        assertDoesNotThrow(
                () ->
                        validator.validate(
                                OrderStatus.SHIPPED,
                                OrderStatus.DELIVERED
                        )
        );
    }


    @Test
    void validateTransition_fromCreatedToShipped_shouldThrowException() {


        assertThrows(
                InvalidStatusTransitionException.class,
                () ->
                        validator.validate(
                                OrderStatus.CREATED,
                                OrderStatus.SHIPPED
                        )
        );
    }


    @Test
    void validateTransition_fromDeliveredToPaid_shouldThrowException() {


        assertThrows(
                InvalidStatusTransitionException.class,
                () ->
                        validator.validate(
                                OrderStatus.DELIVERED,
                                OrderStatus.PAID
                        )
        );
    }
}