package com.takehome.orderservice.sorting;


import com.takehome.orderservice.entity.Order;


import org.springframework.stereotype.Component;


import java.util.Comparator;
import java.util.List;


@Component
public class SortByTotalAmountStrategy
        implements OrderSortingStrategy {


    public String getSortKey() {


        return "totalAmount";
    }


    public List<Order> sort(
            List<Order> orders
    ) {


        return orders.stream()
                .sorted(
                        Comparator.comparing(
                                Order::getTotalAmount
                        )
                )
                .toList();
    }
}