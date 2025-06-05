package com.f1soft.sces.model;

import com.f1soft.sces.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterUser {
  private String fullName;
  private Role role;
  private String phoneNumber;
}
