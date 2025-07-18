package com.example.mainBase.dto;

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
  private BigDecimal grade;
  private String remark;
}
