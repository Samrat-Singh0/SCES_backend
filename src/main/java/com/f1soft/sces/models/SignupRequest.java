package com.f1soft.sces.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
  private String email;
  private String password;
  private String fullName;
  private String address;
  private String phoneNumber;
  private String role;
}
