package com.micro.user.services.impl;

import com.micro.user.exceptions.EmailAlreadyExistsException;
import com.micro.user.exceptions.KeycloakRoleAssignmentException;
import com.micro.user.exceptions.KeycloakRollbackFailureException;
import com.micro.user.exceptions.KeycloakUserCreationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakService {
    private final Keycloak keycloak;

    @Value("${keycloak.admin.realm}")
    private String realm;

    public String createUser(String name, String email, String password) {

        // if exists
        List<UserRepresentation> existing = keycloak.realm(realm)
                .users().searchByEmail(email, true);

        if (!existing.isEmpty()) {
            throw new EmailAlreadyExistsException(email);
        }

        // password
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        // user
        UserRepresentation user = new UserRepresentation();
        String[] parts = name.trim().split(" ", 2);
        user.setFirstName(parts[0]);
        user.setLastName(parts.length > 1 ? parts[1] : "");
        user.setEmail(email);
        user.setUsername(email);
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setCredentials(List.of(credential));

        // create
        Response res = keycloak.realm(realm).users().create(user);
        if (res.getStatus() != 201) {
            throw new KeycloakUserCreationException(email, res.getStatus());
        }

        // extract ID, assign Role
        String location = res.getHeaderString("Location");
        String keycloakId = location.substring(location.lastIndexOf("/") + 1);

        try {
            var role = keycloak.realm(realm).roles().get("USER").toRepresentation();
            keycloak.realm(realm).users().get(keycloakId)
                    .roles().realmLevel().add(List.of(role));
            log.info("Keycloak user created and role assigned: {}", keycloakId);
        } catch (Exception e) {
            log.error("Role assignment failed, rolling back keycloak user {}", keycloakId);
            try {
                keycloak.realm(realm).users().delete(keycloakId);
                log.info("Keycloak user {} rolled back successfully", keycloakId);
            } catch (Exception rollbackEx) {
                log.error("CRITICAL: Keycloak rollback failed for {}. Manual cleanup needed.", keycloakId, rollbackEx);
                throw new KeycloakRollbackFailureException(keycloakId, rollbackEx);
            }
            throw new KeycloakRoleAssignmentException(keycloakId, e);
        }

        return keycloakId;
    }

    public void deleteUser(String keycloakId) {
        keycloak.realm(realm).users().delete(keycloakId);
        log.info("Keycloak user '{}' deleted (rollback)", keycloakId);
    }
}
