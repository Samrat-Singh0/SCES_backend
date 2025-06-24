package com.example.mainBase.dto;

import com.example.mainBase.enums.EnrollStatus;
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
