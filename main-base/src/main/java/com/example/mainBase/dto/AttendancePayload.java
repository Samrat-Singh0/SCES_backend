package com.example.mainBase.dto;

import com.example.attendance_fee_lib.enums.AttendanceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AttendancePayload {

  private String code;

  @NotNull(message = "Student cannot be empty")
  private StudentPayload student;

  @NotNull(message = "Course cannot be empty")
  private CoursePayload course;

  private AttendanceStatus attendanceStatus;

}
