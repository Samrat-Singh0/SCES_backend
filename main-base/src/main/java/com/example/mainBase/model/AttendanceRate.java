package com.example.mainBase.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AttendanceRate {
  private String studentCode;
  private Long presentDays;
  private Long totalDays;
}
