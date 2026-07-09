package com.takehome.orderservice.sorting;


import com.takehome.orderservice.entity.Order;


import java.util.List;


public interface OrderSortingStrategy {


    String getSortKey();


    List<Order> sort(
            List<Order> orders
    );
}