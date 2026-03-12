package com.micro.user.exceptions;


import org.springframework.http.HttpStatus;

// ── 404 Not Found ────────────────────────────────────────────────────────────

public class UserNotFoundException extends BaseException {
    private static final String ERROR_CODE = "USER_NOT_FOUND";

    public UserNotFoundException(Long id) {
        super("User not found with id: " + id, HttpStatus.NOT_FOUND, ERROR_CODE);
    }

    public UserNotFoundException(String keycloakId) {
        super("User not found with keycloakId: " + keycloakId, HttpStatus.NOT_FOUND, ERROR_CODE);
    }
}