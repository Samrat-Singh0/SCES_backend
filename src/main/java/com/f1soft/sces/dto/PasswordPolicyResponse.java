package com.f1soft.sces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordPolicyResponse {
  String policy_code;
  String parameters;
  String regex;
  boolean active;
}
