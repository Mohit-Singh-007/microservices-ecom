package com.micro.user.services;

import com.micro.user.dto.userDTO.RegisterUserReq;
import com.micro.user.dto.userDTO.UpdateUserReq;
import com.micro.user.dto.userDTO.UserRes;

public interface UserServiceInterface {
  UserRes registerUser(RegisterUserReq req);

  UserRes getUserProfile(String keycloakId);

  UserRes updateUserProfile(String keycloakId, UpdateUserReq req);
}
