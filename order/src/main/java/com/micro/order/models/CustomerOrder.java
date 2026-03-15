package com.micro.order.models;

import com.micro.order.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


    @Entity
    @Table(name = "order_table")
    @NoArgsConstructor
    @Getter
    @Setter
    public class CustomerOrder {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long orderId;

        // owner of order
        @Column(nullable = false)
        private String userId;


        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        OrderStatus orderStatus = OrderStatus.PENDING;

        @Column(nullable = false)
        @DecimalMin("0.0")
        private BigDecimal totalAmount;

        // order-items
        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<OrderItems> items = new ArrayList<>();



        // address snapshot
        private String street;
        private String city;
        private String country;
        private String pincode;
        private String state;


        @CreationTimestamp
        private LocalDateTime createdAt;
        @UpdateTimestamp
        private LocalDateTime updatedAt;
    }

