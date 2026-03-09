package com.micro.cart.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCartItemReq {
    @Min(value = 1,message = "Quantity should at least be 1...")
    private int quantity;
}
