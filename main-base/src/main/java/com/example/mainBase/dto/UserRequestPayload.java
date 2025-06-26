package com.example.mainBase.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestPayload {

  private String code;
  private String firstName;
  private String middleName;
  private String lastName;

  @Email(message = "Invalid email format")
  private String email;
  private String address;

  @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits.")
  private String phoneNumber;
  private String role;
  private boolean mustChangePassword;
}
