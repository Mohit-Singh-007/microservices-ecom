package com.micro.product.dto.categoryDTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryRes {
        private Long id;
        private String name;
        private String description;
        private boolean isActive;
        private LocalDateTime createdAt;
}
