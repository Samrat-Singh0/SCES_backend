package com.example.mainBase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FeeRequestPayload {
  private EnrollmentResponsePayload enrollmentPayload;
}
