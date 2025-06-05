package com.f1soft.sces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  UserDto {
  private String userCode;
  private String fullName;
  private String email;
  private String address;
  private String phoneNumber;
  private String role;
  private boolean mustChangePassword;
}
