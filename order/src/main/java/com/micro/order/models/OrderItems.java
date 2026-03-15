package com.micro.order.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    // reference of product
    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal subTotal;


    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",nullable = false)
    private CustomerOrder order;
}
