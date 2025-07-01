package com.example.reportingAndAnalytics.anaytics.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnalyticDto {
  private Long totalCourse;
  private Long totalStudent;
  private Long totalInstructor;
  private BigDecimal totalFee;
  private BigDecimal totalOutstandingFee;
}
