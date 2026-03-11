package com.micro.user.services;

import com.micro.user.dto.userDTO.RegisterUserReq;
import com.micro.user.dto.userDTO.UpdateUserReq;
import com.micro.user.dto.userDTO.UserRes;
import org.springframework.security.oauth2.jwt.Jwt;

public interface UserServiceInterface {
  UserRes registerUser(RegisterUserReq req);

  UserRes getUserProfile(Jwt jwt);

  UserRes updateUserProfile(String keycloakId, UpdateUserReq req);
}
