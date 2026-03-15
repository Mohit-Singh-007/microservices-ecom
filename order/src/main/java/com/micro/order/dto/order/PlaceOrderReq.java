package com.micro.order.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// address needed
@Data
public class PlaceOrderReq {
    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Pincode is required")
    private String pincode;

    @NotBlank(message = "Country is required")
    private String country;
}
