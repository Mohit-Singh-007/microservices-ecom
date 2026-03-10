package com.micro.user.dto.addressDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AddressReq {

    @NotBlank(message = "street is required...")
    private String street;

    @NotBlank(message = "state is required...")
    private String state;

    @NotBlank(message = "city is required...")
    private String city;

    @NotBlank(message = "pincode is required...")
    @Pattern(regexp = "^[0-9]{6}$" , message = "pincode must be 6 digits...")
    private String pincode;

    @NotBlank(message = "street is required...")
    private String country;

    private boolean isDefault=false;

}
