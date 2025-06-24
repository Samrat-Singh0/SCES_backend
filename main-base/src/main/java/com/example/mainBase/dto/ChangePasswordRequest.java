package com.example.mainBase.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
  private String email;
  private String newPassword;
}
