package com.example.mainBase.dto;

import com.example.attendance_fee_lib.enums.AttendanceStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AttendancePayload {

  @NotBlank(message = "Code is required")
  private String code;
  private StudentPayload student;
  private CoursePayload course;
  private AttendanceStatus attendanceStatus;
}
