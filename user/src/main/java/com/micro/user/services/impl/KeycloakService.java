package com.micro.user.services.impl;

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
            throw new IllegalStateException("Email already exists...");
        }

        // password
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);

        // user
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setEmail(email);
        user.setUsername(email);
        user.setFirstName(name);
        user.setEmailVerified(true);
        user.setCredentials(List.of(credential));

        // create

        Response res = keycloak.realm(realm).users().create(user);
        if (res.getStatus() != 201) {
            throw new RuntimeException("User creation failed...");
        }

        // extract ID , assign Role
        String location = res.getHeaderString("Location");
        String keycloakId = location.substring(location.lastIndexOf("/") + 1);

        try {
            var role = keycloak.realm(realm).roles().get("USER").toRepresentation();
            keycloak.realm(realm).users().get(keycloakId)
                    .roles().realmLevel().add(List.of(role));
            log.info("Keycloak user created and role assigned: {}", keycloakId);
        } catch (Exception e) {
            log.error("User created in Keycloak but failed to assign 'USER' role for user {}. Error: {}", keycloakId,
                    e.getMessage());
            throw new RuntimeException("Failed to assign user role in Keycloak. Please check client permissions.", e);
        }

        return keycloakId;

    }
}
