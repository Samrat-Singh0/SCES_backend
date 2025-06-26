package com.example.mainBase.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserResponse {

  private String role;

  @Email(message = "Invalid email format")
  private String email;
  private String firstName;
  private String middleName;
  private String lastName;
  private Boolean mustChangePassword;
}
