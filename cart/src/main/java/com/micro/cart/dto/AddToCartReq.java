package com.micro.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToCartReq {

    @NotNull(message = "Product ID is required...")
    private Long productId;

    @Min(value = 1,message = "Quantity should be at least 1...")
    private int quantity=1;
}
