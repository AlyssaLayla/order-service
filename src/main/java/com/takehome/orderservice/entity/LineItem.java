package com.takehome.orderservice.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineItem {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    private String productName;


    private Integer quantity;


    private BigDecimal unitPrice;
}