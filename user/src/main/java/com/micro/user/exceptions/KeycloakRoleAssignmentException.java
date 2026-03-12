package com.micro.user.exceptions;


import org.springframework.http.HttpStatus;

public class KeycloakRoleAssignmentException extends BaseException {
    private static final String ERROR_CODE = "KEYCLOAK_ROLE_ASSIGNMENT_FAILED";

    public KeycloakRoleAssignmentException(String keycloakId, Throwable cause) {
        super(
                String.format("Failed to assign role to Keycloak user '%s'", keycloakId),
                HttpStatus.BAD_GATEWAY,
                ERROR_CODE,
                cause
        );
    }
}