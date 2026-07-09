package com.takehome.orderservice.sorting;


import com.takehome.orderservice.entity.Order;

import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;


class OrderSorterTest {


    @Test
    void sortByTotalAmount_shouldReturnSortedOrders() {


        Order expensive =
                Order.builder()
                        .totalAmount(
                                BigDecimal.valueOf(100)
                        )
                        .build();


        Order cheap =
                Order.builder()
                        .totalAmount(
                                BigDecimal.valueOf(10)
                        )
                        .build();


        OrderSorter sorter =
                new OrderSorter(
                        List.of(
                                new SortByTotalAmountStrategy()
                        )
                );


        List<Order> result =
                sorter.sort(
                        List.of(
                                expensive,
                                cheap
                        ),
                        "totalAmount"
                );


        assertEquals(
                cheap,
                result.get(0)
        );
    }
}