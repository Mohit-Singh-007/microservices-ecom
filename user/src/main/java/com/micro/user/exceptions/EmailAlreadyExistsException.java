package com.micro.user.exceptions;


import org.springframework.http.HttpStatus;


public class EmailAlreadyExistsException extends BaseException {
    private static final String ERROR_CODE = "USER_EMAIL_ALREADY_EXISTS";

    public EmailAlreadyExistsException(String email) {
        super("Email already registered: " + email, HttpStatus.CONFLICT, ERROR_CODE);
    }
}
