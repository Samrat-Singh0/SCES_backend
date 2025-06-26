package com.example.mainBase.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CourseReportDto {

  private String courseName;
  private String semester;
  private Long totalEnrollments;
  private Long runningEnrollments;
  private BigDecimal averageGrade;
}
