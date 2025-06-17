package com.f1soft.sces.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterUser {

  private String firstName;
  private String middleName;
  private String lastName;
  private String role;
  private String phoneNumber;
}
