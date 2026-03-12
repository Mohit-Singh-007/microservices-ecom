package com.micro.user.services.impl;

import com.micro.user.dto.addressDTO.AddressRes;
import com.micro.user.dto.userDTO.RegisterUserReq;
import com.micro.user.dto.userDTO.UpdateUserReq;
import com.micro.user.dto.userDTO.UserRes;
import com.micro.user.exceptions.EmailAlreadyExistsException;
import com.micro.user.exceptions.KeycloakRollbackFailureException;
import com.micro.user.exceptions.UserRegistrationException;
import com.micro.user.models.User;
import com.micro.user.repository.UserRepo;
import com.micro.user.services.UserServiceInterface;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserServiceInterface {

  private final UserRepo userRepo;
  private final KeycloakService keycloak;

  @Override
  @Transactional
  public UserRes registerUser(RegisterUserReq req) {

    // check if email already exists locally
    if (userRepo.findByEmail(req.getEmail()).isPresent()) {
      throw new EmailAlreadyExistsException(req.getEmail());
    }

    String keycloakId = keycloak.createUser(
            req.getName(),
            req.getEmail(),
            req.getPassword()
    );

    // save user locally
    try {
      User user = new User();
      user.setKeycloakId(keycloakId);
      user.setName(req.getName());
      user.setEmail(req.getEmail());
      user.setPhone(req.getPhone());
      User saved = userRepo.save(user);
      return mapToUserRes(saved);

    } catch (Exception e) {
      log.error("DB save failed, rolling back keycloak user {}", keycloakId);
      try {
        keycloak.deleteUser(keycloakId);
        log.info("Keycloak user {} rolled back successfully", keycloakId);
      } catch (Exception rollbackEx) {
        log.error("CRITICAL: Keycloak rollback failed for {}. Manual cleanup needed.", keycloakId, rollbackEx);
        throw new KeycloakRollbackFailureException(keycloakId, rollbackEx);
      }
      throw new UserRegistrationException("DB save failed; Keycloak user was rolled back", e);
    }
  }

  // get or create from JWT
  @Transactional
  public User getOrCreateUser(Jwt jwt) {
    String keycloakId = jwt.getSubject();

    return userRepo.findByKeycloakId(keycloakId)
            .orElseGet(() -> {
              User user = new User();
              user.setKeycloakId(keycloakId);
              user.setName(jwt.getClaimAsString("name") != null
                      ? jwt.getClaimAsString("name")
                      : jwt.getClaimAsString("preferred_username")
              );
              user.setEmail(jwt.getClaimAsString("email"));
              return userRepo.save(user);
            });
  }

  @Override
  @Transactional
  public UserRes getUserProfile(Jwt jwt) {
    return mapToUserRes(getOrCreateUser(jwt));
  }

  @Override
  @Transactional
  public UserRes updateUserProfile(Jwt jwt, UpdateUserReq req) {

    // user hai ya ni
    User user = getOrCreateUser(jwt);
    if(req.getName() != null){
      user.setName(req.getName());
    }
    if(req.getPhone() != null){
      user.setPhone(req.getPhone());
    }
    if(req.getProfileImageURL() != null){
      user.setProfileImageURL(req.getProfileImageURL());
    }
    return mapToUserRes(userRepo.save(user));
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