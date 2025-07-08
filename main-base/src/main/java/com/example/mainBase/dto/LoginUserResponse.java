package com.example.mainBase.dto;

import jakarta.validation.constraints.Email;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginUserResponse {

  private String role;

  @Email(message = "Invalid email format")
  private String email;

  private String firstName;

  private String middleName;

  private String lastName;

  private Boolean mustChangePassword;

  private Date accessExpiryDate;

  private Date refreshExpiryDate;
}
