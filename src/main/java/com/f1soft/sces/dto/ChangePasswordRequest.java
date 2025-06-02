package com.f1soft.sces.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
  private String email;
  private String newPassword;
}
