package com.micro.user.exceptions;


import org.springframework.http.HttpStatus;

/**
 * Thrown when a Keycloak user was created but the local DB save failed,
 * AND the compensating delete from Keycloak also failed.
 *
 * This is a CRITICAL state — the keycloakId should be persisted to a
 * dead-letter / orphan table for manual reconciliation.
 */
public class KeycloakRollbackFailureException extends BaseException {
    private static final String ERROR_CODE = "KEYCLOAK_ROLLBACK_FAILED";

    private final String orphanedKeycloakId;

    public KeycloakRollbackFailureException(String keycloakId, Throwable cause) {
        super(
                String.format(
                        "CRITICAL: Keycloak user '%s' was created but DB save failed, " +
                                "and Keycloak rollback also failed. Manual cleanup required.", keycloakId),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ERROR_CODE,
                cause
        );
        this.orphanedKeycloakId = keycloakId;
    }

    public String getOrphanedKeycloakId() {
        return orphanedKeycloakId;
    }
}