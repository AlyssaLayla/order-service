package com.takehome.orderservice.sorting;


import com.takehome.orderservice.entity.Order;


import org.springframework.stereotype.Component;


import java.util.List;


@Component
public class OrderSorter {


    private final List<OrderSortingStrategy> strategies;


    public OrderSorter(
            List<OrderSortingStrategy> strategies
    ) {


        this.strategies =
                strategies;
    }


    public List<Order> sort(
            List<Order> orders,
            String key
    ) {


        return strategies.stream()
                .filter(
                        strategy ->
                                strategy.getSortKey()
                                        .equals(
                                                key
                                        )
                )
                .findFirst()
                .orElseThrow()
                .sort(
                        orders
                );
    }
}