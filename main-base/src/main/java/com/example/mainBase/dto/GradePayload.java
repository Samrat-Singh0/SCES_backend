package com.example.mainBase.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GradePayload {

  private String code;

  private StudentPayload student;
  private CoursePayload course;

  @Min(value = 0L,message = "Grades cannot be negative")
  @Max(value = 60L, message = "Grades cannot exceed 60.00")
  private BigDecimal grade;

  private String remark;
}
