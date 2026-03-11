package com.micro.user.controllers;

import com.micro.user.dto.userDTO.RegisterUserReq;
import com.micro.user.dto.userDTO.UpdateUserReq;
import com.micro.user.dto.userDTO.UserRes;
import com.micro.user.services.UserServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserServiceInterface userService;

  // public - no auth needed
  @PostMapping("/register")
  public ResponseEntity<UserRes> register(@Valid @RequestBody RegisterUserReq req) {
    UserRes res = userService.registerUser(req);
    return ResponseEntity.status(HttpStatus.CREATED).body(res);
  }

  // authenticated - gets profile using keycloakId from JWT
  @GetMapping("/me")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserRes> getMyProfile(@AuthenticationPrincipal Jwt jwt) {
    UserRes res = userService.getUserProfile(jwt);
    return ResponseEntity.ok(res);
  }

  // authenticated - update own profile
  @PatchMapping("/me")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserRes> updateMyProfile(@AuthenticationPrincipal Jwt jwt,
      @Valid @RequestBody UpdateUserReq req) {
    String keycloakId = jwt.getSubject();
    UserRes res = userService.updateUserProfile(keycloakId, req);
    return ResponseEntity.ok(res);
  }
}
