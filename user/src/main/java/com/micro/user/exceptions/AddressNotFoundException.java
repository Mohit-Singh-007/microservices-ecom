package com.micro.user.exceptions;

import org.springframework.http.HttpStatus;

public class AddressNotFoundException extends BaseException {
    private static final String ERROR_CODE = "ADDRESS_NOT_FOUND";

    public AddressNotFoundException(Long addressId) {
        super("Address not found with id: " + addressId, HttpStatus.NOT_FOUND, ERROR_CODE);
    }
}

