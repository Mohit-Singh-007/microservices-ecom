package com.micro.user.exceptions;
import org.springframework.http.HttpStatus;

public class UserRegistrationException extends BaseException {
  private static final String ERROR_CODE = "USER_REGISTRATION_FAILED";

  public UserRegistrationException(String message, Throwable cause) {
    super(message, HttpStatus.INTERNAL_SERVER_ERROR, ERROR_CODE, cause);
  }
}