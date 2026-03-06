package com.micro.product.dto.productDTO;

import lombok.Data;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

@Data
public class ProductReq {

    @NotBlank
    private String name;

    private String description;

    // never expose actual entities
    private Long categoryId;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal price;

    @NotBlank
    private String sku;

    private String brand;

    private String imageUrl;
}