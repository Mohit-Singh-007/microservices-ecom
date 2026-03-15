package com.micro.order.dto.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRes {
    private Long orderItemId;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal subTotal;
}
