package com.example.mainBase.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GradeReportDto {
  private String studentName;
  private String completionStatus;
  private BigDecimal grade;
}
