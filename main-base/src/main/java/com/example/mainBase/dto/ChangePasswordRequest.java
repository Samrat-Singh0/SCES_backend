package com.example.mainBase.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ChangePasswordRequest {

  @Email(message="Invalid email format")
  private String email;
  private String newPassword;
}
