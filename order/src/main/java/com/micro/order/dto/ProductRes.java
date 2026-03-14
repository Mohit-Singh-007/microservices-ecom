package com.micro.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRes {
    private Long id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private String sku;
    private String brand;
    private String imageUrl;
    private boolean isActive;
}
