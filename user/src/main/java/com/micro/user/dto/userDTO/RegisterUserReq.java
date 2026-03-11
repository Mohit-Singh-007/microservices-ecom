package com.micro.user.dto.userDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserReq {

  @NotBlank(message = "name is required...")
  private String name;

  @NotBlank(message = "email is required...")
  @Email(message = "invalid email format...")
  private String email;

  @NotBlank(message = "password is required...")
  @Size(min = 6, message = "password must be at least 6 characters...")
  private String password;

  @NotBlank(message = "phone num is required...")
  @Size(min = 10, message = "number must be at least 10 characters...")
  private String phone;
}
