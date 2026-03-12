package com.micro.user.exceptions;

import org.springframework.http.HttpStatus;


    public class AddressOwnershipException extends BaseException {
        private static final String ERROR_CODE = "ADDRESS_OWNERSHIP_DENIED";

        public AddressOwnershipException(Long addressId) {
            super("Address " + addressId + " does not belong to the current user",
                    HttpStatus.FORBIDDEN, ERROR_CODE);
        }
    }
