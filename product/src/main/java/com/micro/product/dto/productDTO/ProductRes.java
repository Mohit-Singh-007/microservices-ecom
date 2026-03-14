package com.micro.product.dto.productDTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
        private LocalDateTime createdAt;
        private int stock;
    }
