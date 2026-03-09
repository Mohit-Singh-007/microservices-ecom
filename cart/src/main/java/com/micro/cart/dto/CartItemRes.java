package com.micro.cart.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CartItemRes {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal subTotal;
    private LocalDateTime createdAt;
}
