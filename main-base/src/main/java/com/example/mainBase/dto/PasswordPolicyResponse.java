package com.example.mainBase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordPolicyResponse {

  String code;
  String parameters;
  String regex;
  boolean active;
}
