package com.example.mainBase.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FeePayload {

  private EnrollmentResponsePayload enrollmentPayload;
  private BigDecimal amount;
}
