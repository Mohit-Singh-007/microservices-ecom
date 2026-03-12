package com.micro.user.exceptions;
import org.springframework.http.HttpStatus;


public class KeycloakUserCreationException extends BaseException {
    private static final String ERROR_CODE = "KEYCLOAK_USER_CREATION_FAILED";

    public KeycloakUserCreationException(String email, int statusCode) {
        super(
                String.format("Keycloak rejected user creation for '%s' (HTTP %d)", email, statusCode),
                HttpStatus.BAD_GATEWAY,
                ERROR_CODE
        );
    }
}