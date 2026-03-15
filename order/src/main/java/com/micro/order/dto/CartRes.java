package com.micro.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartRes {
    private Long cartId;
    private List<CartItemRes> items;
    private BigDecimal total;
    private int totalItems;
}
