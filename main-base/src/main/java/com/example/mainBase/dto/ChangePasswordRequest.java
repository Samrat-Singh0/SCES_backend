package com.example.mainBase.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordRequest {

  @Email(message="Invalid email format")
  private String email;

  @NotNull(message = "Password is required")
  private String newPassword;
}
