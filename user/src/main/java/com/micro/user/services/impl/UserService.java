package com.micro.user.services.impl;

import com.micro.user.dto.addressDTO.AddressRes;
import com.micro.user.dto.userDTO.RegisterUserReq;
import com.micro.user.dto.userDTO.UpdateUserReq;
import com.micro.user.dto.userDTO.UserRes;
import com.micro.user.models.User;
import com.micro.user.repository.UserRepo;
import com.micro.user.services.UserServiceInterface;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {

  private final UserRepo userRepo;
  private final Keycloak keycloak;

  @Value("${keycloak.admin.user-realm}")
  private String userRealm;

  @Override
  public UserRes registerUser(RegisterUserReq req) {

    // check if email already exists locally
    if (userRepo.findByEmail(req.getEmail()).isPresent()) {
      throw new IllegalStateException("Email already registered...");
    }

    // 1. create user in keycloak
    UserRepresentation kcUser = new UserRepresentation();
    kcUser.setUsername(req.getEmail());
    kcUser.setEmail(req.getEmail());
    kcUser.setFirstName(req.getName());
    kcUser.setEnabled(true);
    kcUser.setEmailVerified(true);

    // set password
    CredentialRepresentation credential = new CredentialRepresentation();
    credential.setType(CredentialRepresentation.PASSWORD);
    credential.setValue(req.getPassword());
    credential.setTemporary(false);
    kcUser.setCredentials(List.of(credential));

    // 2. call keycloak admin api
    UsersResource usersResource = keycloak.realm(userRealm).users();
    Response response = usersResource.create(kcUser);

    if (response.getStatus() == 409) {
      throw new IllegalStateException("User already exists in keycloak...");
    }

    if (response.getStatus() != 201) {
      throw new RuntimeException("Failed to create user in keycloak: " + response.getStatusInfo());
    }

    // 3. extract keycloak id from location header
    String locationHeader = response.getHeaderString("Location");
    String keycloakId = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);

    // 4. save user locally
    User user = new User();
    user.setKeycloakId(keycloakId);
    user.setName(req.getName());
    user.setEmail(req.getEmail());
    userRepo.save(user);

    return mapToUserRes(user);
  }

  @Override
  public UserRes getUserProfile(String keycloakId) {
    User user = userRepo.findByKeycloakId(keycloakId)
        .orElseThrow(() -> new EntityNotFoundException("User not found..."));
    return mapToUserRes(user);
  }

  @Override
  public UserRes updateUserProfile(String keycloakId, UpdateUserReq req) {
    User user = userRepo.findByKeycloakId(keycloakId)
        .orElseThrow(() -> new EntityNotFoundException("User not found..."));

    if (req.getName() != null)
      user.setName(req.getName());
    if (req.getPhone() != null)
      user.setPhone(req.getPhone());
    if (req.getProfileImageURL() != null)
      user.setProfileImageURL(req.getProfileImageURL());

    userRepo.save(user);
    return mapToUserRes(user);
  }

  private UserRes mapToUserRes(User user) {
    UserRes res = new UserRes();
    res.setId(user.getId());
    res.setKeycloakId(user.getKeycloakId());
    res.setName(user.getName());
    res.setEmail(user.getEmail());
    res.setPhone(user.getPhone());
    res.setActive(user.isActive());
    res.setCreatedAt(user.getCreatedAt());

    if (user.getAddressList() != null) {
      res.setAddressRes(user.getAddressList().stream().map(a -> {
        AddressRes ar = new AddressRes();
        ar.setId(a.getId());
        ar.setStreet(a.getStreet());
        ar.setCity(a.getCity());
        ar.setState(a.getState());
        ar.setPincode(a.getPincode());
        ar.setCountry(a.getCountry());
        ar.setDefault(a.isDefault());
        ar.setCreatedAt(a.getCreatedAt());
        return ar;
      }).toList());
    }

    return res;
  }
}
