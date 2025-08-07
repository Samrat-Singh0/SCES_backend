package com.example.mainBase.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordPolicyRequest {

  @NotBlank(message = "Code is required")
  String code;

  boolean active;

  @Min(value = 0, message = "Length of the policy cannot be negative")
  int length;
}
