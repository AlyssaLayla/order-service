package com.takehome.orderservice.entity;


import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;


    private String customerName;


    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<LineItem> items;


    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    private BigDecimal totalAmount;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist() {

        if (status == null) {
            status = OrderStatus.CREATED;
        }


        createdAt =
                LocalDateTime.now();


        updatedAt =
                LocalDateTime.now();
    }


    @PreUpdate
    public void preUpdate() {

        updatedAt =
                LocalDateTime.now();
    }
}