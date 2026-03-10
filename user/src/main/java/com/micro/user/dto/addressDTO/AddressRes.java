package com.micro.user.dto.addressDTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddressRes {
    private Long id;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private boolean isDefault;

    private LocalDateTime createdAt;

}
