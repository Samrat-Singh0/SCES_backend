package com.example.mainBase.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SemesterPayload {

  private String label;
  private BigDecimal fee;
  private LocalDate startDate;
  private LocalDate endDate;
}
