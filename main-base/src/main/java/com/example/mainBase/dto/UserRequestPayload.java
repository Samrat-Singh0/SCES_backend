package com.example.mainBase.dto;

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
  private String email;
  private String address;
  private String phoneNumber;
  private String role;
  private boolean mustChangePassword;
}
