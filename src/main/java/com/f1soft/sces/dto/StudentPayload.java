package com.f1soft.sces.dto;

import com.f1soft.sces.enums.EnrollStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentPayload {

  private String code;
  private UserRequestPayload user;
  private EnrollStatus enrollStatus;
}
